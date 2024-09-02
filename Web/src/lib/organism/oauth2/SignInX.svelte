<script lang="ts">
  import { signInWithOtherProvider } from "../../../service/AuthService";
  import { Button } from "sveltestrap";
  import xLogo from "../../../assets/logo-x.png";
  import { useNavigate } from "svelte-navigator";
  import { onAddUser } from "../../../service/UserService";

  export let isNewAccount: boolean;

  const navigate = useNavigate();

  const handleSignIn = async () => {
    try {
      const result = await signInWithOtherProvider("xtwitter");

      if (result) {
        console.log("Authentication X réussie");

        if (isNewAccount) {
          onAddUser();
        }

        navigate("/");
      } else {
        console.log("Problème Authentication X");
      }
    } catch (error) {
      console.error("Erreur durant la connexion x:", error);
    }
  };
</script>

<Button
  class="w-75 m-auto"
  style="display:flex;justify-content:center;align-items:center;margin-bottom:0.5rem !important;"
  color="light"
  type="button"
  on:click={handleSignIn}
  ><img src={xLogo} alt="Logo X" />Continuer avec X</Button
>

<style>
  img {
    margin-right: 2px;
    width: 20px;
    height: 20px;
  }
</style>
