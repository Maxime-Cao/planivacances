<script lang="ts">
  import { useNavigate } from "svelte-navigator";
  import { Form, FormGroup, Input, Button } from "sveltestrap";
  import { Link } from "svelte-navigator";
  import { register } from "../../../service/AuthService";
  import SignInGoogle from "../../organism/oauth2/SignInGoogle.svelte";
  import InputPasswordWithToggle from "../../molecule/InputPasswordWithToggle.svelte";
  import SignInFacebook from "../../organism/oauth2/SignInFacebook.svelte";
  import SignInX from "../../organism/oauth2/SignInX.svelte";
  import { onAddUser } from "../../../service/UserService";

  let validated: boolean = false;
  let errorMessage: string|null = null;

  let isNewAccount: boolean = true;

  const navigate = useNavigate();

  function onSubmit(e: any) {
    e.preventDefault();

    const formData = new FormData(e.target);

    const password = formData.get("password") as string;
    const passwordRepeat = formData.get("passwordRepeat") as string;

    if(password == passwordRepeat) {
      validated = true;
      errorMessage = null;

      register(
        formData.get("name") as string, 
        formData.get("surname") as string, 
        formData.get("email") as string, 
        formData.get("password") as string)
      .then((result) => {
        if (result) {

          onAddUser();

          navigate("/");
        } else {
          errorMessage = "Problème lors de la création du compte"
        }
      });
    } else {
      errorMessage = "Problème de correspondance des mots de passe"
    }
  }
</script>

<h1>Création d'un compte</h1>
<section id="registerForm">
  <Form action="POST" {validated} on:submit={onSubmit}>
    <FormGroup floating label="Nom">
      <Input 
      name="name" 
      placeholder="Nom" 
      feedback="Nom requis" 
      required />
    </FormGroup>
    <FormGroup floating label="Prénom">
      <Input
        name="surname"
        placeholder="Prénom"
        feedback="Prénom requis"
        required
      />
    </FormGroup>
    <FormGroup floating label="Mail">
      <Input
        type="email"
        name="email"
        placeholder="Email"
        feedback="Addresse mail requise"
        required
      />
    </FormGroup>
    <InputPasswordWithToggle validated={validated} />
    <InputPasswordWithToggle validated={validated} name="passwordRepeat"/>

    {#if errorMessage != null}
      <p id="errorMessage">{errorMessage}</p>
    {/if}

    <Link to="/connection" class="primary">Vous avez déjà un compte ?</Link>
    <Button color="primary" class="w-75 mb-3 mt-3">Créer un compte</Button>
    <SignInGoogle {isNewAccount} />
    <SignInFacebook {isNewAccount} />
    <SignInX {isNewAccount} />
  </Form>
</section>

<style>
  #registerForm {
    margin: 2rem auto;
    width: 35rem;
  }

  #errorMessage {
    color: lightcoral;
  }

</style>
