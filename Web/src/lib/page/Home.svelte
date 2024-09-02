<script lang="ts">
  import RegistredMembersSection from "../organism/RegistredMembersSection.svelte";
  import MembersInHolidayByDateSection from "../organism/MembersInHolidayByDateSection.svelte";
</script>

<p id="catchPhrase" class="text-primary">
  Voyagez mieux, ensemble.<br />Organisez vos vacances en un clin d'œil !
</p>

<RegistredMembersSection />

<MembersInHolidayByDateSection />

<style>
  #catchPhrase {
    font-size: 1.5rem;
    font-weight: 600;
  }
</style>
