<script lang="ts">
  import { signInWithOtherProvider } from "../../../service/AuthService";
  import { Button } from "sveltestrap";
  import facebookLogo from "../../../assets/logo-facebook.png";
  import { useNavigate } from "svelte-navigator";
  import { onAddUser } from "../../../service/UserService";

  export let isNewAccount: boolean;

  const navigate = useNavigate();

  const handleSignIn = async () => {
    try {
      const result = await signInWithOtherProvider("facebook");

      if (result) {
        console.log("Authentication Facebook réussie");

        if (isNewAccount) {
          onAddUser();
        }

        navigate("/");
      } else {
        console.log("Problème Authentication Facebook");
      }
    } catch (error) {
      console.error("Erreur durant la connexion facebook:", error);
    }
  };
</script>

<Button
  class="w-75 m-auto"
  style="display:flex;justify-content:center;align-items:center;margin-bottom:0.5rem !important;"
  color="light"
  type="button"
  on:click={handleSignIn}
  ><img src={facebookLogo} alt="Logo Facebook" />Continuer avec Facebook</Button
>

<style>
  img {
    margin-right: 2px;
    width: 20px;
    height: 20px;
  }
</style>
