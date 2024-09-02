<script lang="ts">
  import { Link } from "svelte-navigator";
  import HolidaysSection from "../../organism/HolidaysSection.svelte";
</script>

<div id="addHoliday">
  <Link to="/newHoliday">
    <i class="fa-solid fa-plus"></i>
  </Link>
</div>

<h1>Mes vacances</h1>

<HolidaysSection />

<style>
  #addHoliday {
    text-align: end;
  }

  #addHoliday i {
    font-size: 3.5em;
    color: black;
  }
</style>