<script lang="ts">
  import { useNavigate } from "svelte-navigator";
  import { Button } from "sveltestrap";
  import { format } from "date-fns";
  import Calendar from "../../../assets/calendrier.png";
  import Weather from "../../../assets/meteo.png";
  import Tchat from "../../../assets/tchat.png";
  import { groupListStore } from "../../../stores/groups";
  import type { GroupMap } from "../../../model/GroupMap";
  import { currentGidStore as currentGidStore } from "../../../stores/currentGroup";
  import { deleteGroup, sendGroupInvite } from "../../../service/GroupService";
  import type { Group } from "../../../model/Group";
  
  const navigate = useNavigate();

  let groups: GroupMap = $groupListStore || {};

  let group: Group;

  let address: string;
  let formattedStartDate: string;
  let formattedEndDate: string;

  currentGidStore.subscribe(value => {
    group = groups[value];

    if(group != null && group.place != null) {
      address = `${group.place.street}, ${group.place.number} ${group.place.city} ${group.place.country}`;
      formattedStartDate = format(new Date(group.startDate ?? ""), "dd/MM/yyyy");
      formattedEndDate = format(new Date(group.endDate ?? ""), "dd/MM/yyyy");
    }
  });

  function addMemberToHoliday() {
    const emailRegex: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  const mail: string | null = window.prompt(
      "Ajout d'un membre au voyage\nEntrez ci-dessous l’email de l’utilisateur à ajouter..."
    );

    if (mail !== null) {
      if (emailRegex.test(mail)) {
        sendGroupInvite(group.gid, mail);
      } else {
        alert("Adresse e-mail invalide. Veuillez entrer une adresse e-mail valide.");
      }
    }
    
  }

  function handleDeleteHoliday() {
    deleteGroup(group.gid).then(value => {
      if(value) {
        navigate("/holidays");
      }
    });
  }

  function handleUpdateHoliday() {
    navigate("/updateHoliday");
  }

  function onGoToPlanning() {
    navigate("/planning");
  }

  function onGoToWeather() {
    navigate("/weather");
  }

  function onGoToTchat() {
    navigate("/tchat");
  }
</script>

<section id="holidayDetails">
  {#if group != null}
    <div id="addMemberIcone">
      <i
        class="fa-solid fa-user-plus fa-2xl"
        on:click={addMemberToHoliday}
        on:keypress={addMemberToHoliday}
      />
    </div>
    <h1>{group.groupName}</h1>
    <h2>Du {formattedStartDate} au {formattedEndDate}</h2>
    <h3>{address}</h3>
    <p>{group.description}</p>
    <div id="moreActionsForHoliday">
      <div
        id="goToPlanning"
        on:click={onGoToPlanning}
        on:keypress={onGoToPlanning}
      >
        <img src={Calendar} alt="Icône calendrier" />
        <h4>Planning</h4>
      </div>
      <div
        id="goToWeather"
        on:click={onGoToWeather}
        on:keypress={onGoToWeather}
      >
        <img src={Weather} alt="Icône météo" />
        <h4>Météo</h4>
      </div>
      <div id="goToTchat" on:click={onGoToTchat} on:keypress={onGoToTchat}>
        <img src={Tchat} alt="Icône tchat" />
        <h4>Tchat</h4>
      </div>
    </div>
    <div>
      <Button color="primary" on:click={handleUpdateHoliday}>Modifier</Button>
      <Button color="danger" on:click={handleDeleteHoliday}>Supprimer</Button>
    </div>
  {:else}
    <h1>Aucun voyage trouvé</h1>
  {/if}
</section>

<style>
  #holidayDetails {
    /*width: 60%;
    margin: auto;*/
  }
  #addMemberIcone {
    margin-bottom: 1rem;

    text-align: end;
  }

  #moreActionsForHoliday {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    padding: 1rem;
    width: 30rem;
    height: auto;
    margin: auto;
    margin-bottom: 2rem;
    background-color: #f2f2f2;
  }

  #moreActionsForHoliday div:hover {
    cursor: pointer;
  }
</style>
