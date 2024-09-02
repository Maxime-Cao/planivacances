package be.helmo.planivacances.service;

import be.helmo.planivacances.model.ConfigurationSmtp;
import be.helmo.planivacances.model.dto.UserDTO;
import be.helmo.planivacances.model.dto.FormContactDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;

import com.google.firebase.cloud.FirestoreClient;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Set<SseEmitter> emitters = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PlaceService placeServices;

    /**
     * Récupère le nombre d'utilisateurs de PlaniVacances
     *
     * @return (int) Nombre d'utilisateurs de PlaniVacances
     * @throws FirebaseAuthException Exception Firebase
     */
    private int getNumberOfUsers() throws FirebaseAuthException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ListUsersPage userPage = auth.listUsers(null);

        int userCount = 0;
        while (userPage != null) {
            for (UserRecord ignored : userPage.getValues()) {
                userCount++;
            }
            userPage = userPage.getNextPage();
        }

        return userCount;
    }

    public SseEmitter getNumberUsersStream() {
        SseEmitter newEmitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(newEmitter);
        return newEmitter;
    }

    public void sendSSEUpdateToEveryone() {
        List<SseEmitter> emittersToRemove = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                int userCount = getNumberOfUsers();
                emitter.send(SseEmitter.event().name("message").data(String.valueOf(userCount),
                        MediaType.TEXT_EVENT_STREAM));
            } catch (Exception e) {
                emitter.complete();
                emittersToRemove.add(emitter);
            }
        }
        emittersToRemove.forEach(emitters::remove);
    }

    public void sendSSEUpdateToSomeone(SseEmitter emitter) {
        if(emitter != null) {
            try {
                int userCount = getNumberOfUsers();
                emitter.send(SseEmitter.event().name("message").data(String.valueOf(userCount),
                        MediaType.TEXT_EVENT_STREAM));
            } catch (Exception e) {
                emitter.complete();
            }
        }
    }

    public Map<String, Integer> getUserCountPerCountry(String givenDate) throws ExecutionException, InterruptedException, ParseException {
        // Map to store counts per country
        Map<String, Integer> countryCounts = new HashMap<>();

        Firestore fdb = FirestoreClient.getFirestore();
        CollectionReference groupsCollection = fdb.collection("groups");

        Set<String> validGroupIds = getGroupsForGivenDate(groupsCollection, givenDate);

        for (String groupId : validGroupIds) {
            ApiFuture<DocumentSnapshot> groupDocument = groupsCollection
                    .document(groupId)
                    .get(FieldMask.of("userCount"));

            if (groupDocument.get().exists()) {
                int userCount = Math.toIntExact(groupDocument.get().getLong("userCount"));

                // Step 4: Retrieve Country Information from the Group
                String country = getCountryForGroupId(groupsCollection, groupId);

                // Step 5: Aggregate Users per Country within Groups
                int currentCount = countryCounts.getOrDefault(country, 0);
                countryCounts.put(country, currentCount + userCount);
            }
        }

        return countryCounts;

    }

    private Set<String> getGroupsForGivenDate(CollectionReference groupsCollection, String givenDate) throws ParseException, ExecutionException, InterruptedException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = dateFormat.parse(givenDate);

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Subtract one day using java.time
        LocalDate previousLocalDate = localDate.plusDays(1);
        Date dateForStart = Date.from(previousLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Step 1: Retrieve Group IDs where startDate is less than or equal to the given date
        QuerySnapshot startDatesSnapshot = groupsCollection
                .whereLessThanOrEqualTo("startDate", dateForStart)
                .get()
                .get();

        // Step 2: Retrieve Group IDs where endDate is greater than or equal to the given date
        QuerySnapshot endDatesSnapshot = groupsCollection
                .whereGreaterThanOrEqualTo("endDate", date)
                .get()
                .get();

        // Combine group IDs from both queries
        Set<String> startGroupIds = startDatesSnapshot.getDocuments().stream()
                .map(QueryDocumentSnapshot::getId)
                .collect(Collectors.toSet());

        Set<String> endGroupIds = endDatesSnapshot.getDocuments().stream()
                .map(QueryDocumentSnapshot::getId)
                .collect(Collectors.toSet());

        // Find intersection of group IDs that satisfy both conditions
        Set<String> validGroupIds = new HashSet<>(startGroupIds);
        validGroupIds.retainAll(endGroupIds);

        return  validGroupIds;
    }

    // Helper method to retrieve the country for a given groupId
    private String getCountryForGroupId(CollectionReference groupsCollection, String groupId) throws ExecutionException, InterruptedException {
        DocumentSnapshot groupDoc = groupsCollection.document(groupId).get().get();
        String pid =  groupDoc.getString("placeID");
        return placeServices.getPlace(groupId, pid).getCountry();
    }


    public boolean deleteUser(String uid) throws FirebaseAuthException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.deleteUser(uid);
        return true;
    }

    private List<UserRecord> getAdminUsers() throws Exception {
        List<UserRecord> adminUsers = new ArrayList<>();

        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        while (page != null) {
            for (UserRecord user : page.iterateAll()) {
                if (user.getCustomClaims() != null && user.getCustomClaims().containsKey("admin")
                        && (boolean) user.getCustomClaims().get("admin")) {
                    adminUsers.add(user);
                }
            }
            page = page.hasNextPage() ? page.getNextPage() : null;
        }

        return adminUsers;
    }

    public String findUserUidByEmail(String email) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        return userRecord.getUid();
    }

    public UserDTO getUser(String uid) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        return new UserDTO(userRecord.getUid(),userRecord.getEmail(),userRecord.getDisplayName());
    }

    public String contactAdmin(FormContactDTO form) throws Exception {
        Resource resource = new ClassPathResource("smtp.json");
        ConfigurationSmtp configuration = objectMapper.readValue(
                resource.getInputStream(),
                ConfigurationSmtp.class
        );

        HtmlEmail email = new HtmlEmail();
        email.setHostName(configuration.getSmtpHost());
        email.setSmtpPort(configuration.getSmtpPort());
        email.setAuthenticator(new DefaultAuthenticator(configuration.getSmtpUsername(), configuration.getSmtpPassword()));
        email.setStartTLSEnabled(true);
        email.setFrom(configuration.getSmtpUsername());
        email.setSubject(form.getSubject());
        email.setMsg(String.format("Message de : %s\n%s",form.getEmail(),form.getMessage()));

        for(UserRecord user : getAdminUsers()) {
            email.addTo(user.getEmail());
        }

        email.send();
        return "E-mails envoyés avec succès !";
    }
}
