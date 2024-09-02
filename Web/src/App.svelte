<script lang="ts">
  import { Router, Route } from "svelte-navigator";
  import Footer from "./lib/atom/Footer.svelte";
  import Header from "./lib/organism/Header.svelte";
  import Home from "./lib/page/Home.svelte";
  import Contact from "./lib/page/Contact.svelte";
  import Connection from "./lib/page/auth/Connection.svelte";
  import Register from "./lib/page/auth/Register.svelte";
  import Holidays from "./lib/page/holiday/Holidays.svelte";
  import NewHoliday from "./lib/page/holiday/NewHoliday.svelte";
  import HolidayDetails from "./lib/page/holiday/HolidayDetails.svelte";
  import UpdateHoliday from "./lib/page/holiday/UpdateHoliday.svelte";
  import ActivitiesPlanning from "./lib/page/activity/ActivitiesPlanning.svelte";
  import ActivityDetails from "./lib/page/activity/ActivityDetails.svelte";
  import NewActivity from "./lib/page/activity/NewActivity.svelte";
  import UpdateActivity from "./lib/page/activity/UpdateActivity.svelte";
  import Tchat from "./lib/page/Tchat.svelte";
  import Weather from "./lib/page/Weather.svelte";
  import NotificationPage from "./lib/page/NotificationPage.svelte";
  import LoadingIndicator from "./lib/molecule/LoadingIndicator.svelte";
  import { loadFcmToken } from "./service/NotificationService";

  let basepath = import.meta.env.BASE_URL;

  loadFcmToken();

</script>

<Router {basepath}>
  <LoadingIndicator />

  <Header />
  
  <main class="app">
    <Route path="/" component={Home} />
    <Route path="/contact" component={Contact} />
    <Route path="/register" component={Register} />
    <Route path="/connection" component={Connection} />
    <Route path="/profile">Mon profil</Route>
    <Route path="/holidays" component={Holidays} />
    <Route path="/newHoliday" component={NewHoliday} />
    <Route path="/updateHoliday" component={UpdateHoliday} />
    <Route path="/holidayDetails" component={HolidayDetails} />
    <Route path="/newActivity" component={NewActivity} />
    <Route path="/updateActivity" component={UpdateActivity} />
    <Route path="/activityDetails" component={ActivityDetails} />
    <Route path="/planning" component={ActivitiesPlanning} />
    <Route path="/tchat" component={Tchat} />
    <Route path="/weather" component={Weather} />
    <Route path="/notifications" component={NotificationPage} />
  </main>
</Router>

<Footer />
