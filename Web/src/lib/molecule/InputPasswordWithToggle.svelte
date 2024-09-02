<script lang="ts">
  import { FormGroup, Input } from "sveltestrap";
  import "@fortawesome/fontawesome-free/css/all.min.css";

  export let name: string = "password";
  export let validated: boolean = false;

  let passwordVisible: boolean = false;

  function togglePasswordVisibility() {
    passwordVisible = !passwordVisible;
  }
</script>

<FormGroup>
  <div id="password-container" class="{validated ? "i-shift" : ""}">
    <Input
      type={passwordVisible ? "text" : "password"}
      name={name}
      placeholder="Mot de passe"
      feedback="Mot de passe requis"
      required
    />
    {#if passwordVisible}
      <i
        class="fa-solid fa-eye-slash"
        on:click={togglePasswordVisibility}
        on:keypress={togglePasswordVisibility}
      />
    {:else}
      <i
        class="fa-solid fa-eye"
        on:click={togglePasswordVisibility}
        on:keypress={togglePasswordVisibility}
      />
    {/if}
  </div>
</FormGroup>

<style>
  #password-container {
    display: flex;
    align-items: center;
  }

  #password-container i {
    margin-left: -30px;
  }

  .i-shift i {
    margin-left: -60px !important;
  }
  
</style>
