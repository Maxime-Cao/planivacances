<script lang="ts">
  import { signInWithOtherProvider } from "../../../service/AuthService";
  import { Button } from "sveltestrap";
  import googleLogo from "../../../assets/logo-google.png";
  import { useNavigate } from "svelte-navigator";
  import { onAddUser } from "../../../service/UserService";

  export let isNewAccount: boolean;

  const navigate = useNavigate();

  const handleSignIn = async () => {
    try {
      const result = await signInWithOtherProvider("google");

      if (result) {
        console.log("Authentication Google réussie");

        if (isNewAccount) {
          onAddUser();
        }

        navigate("/");
      } else {
        console.log("Problème Authentication Google");
      }
    } catch (error) {
      console.error("Erreur durant la connexion google:", error);
    }
  };
</script>

<Button
  class="w-75 m-auto"
  style="display:flex;justify-content:center;align-items:center;margin-bottom:0.5rem !important;"
  color="light"
  type="button"
  on:click={handleSignIn}
  ><img src={googleLogo} alt="Logo Google" />Continuer avec Google</Button
>

<style>
  img {
    margin-right: 2px;
  }
</style>
