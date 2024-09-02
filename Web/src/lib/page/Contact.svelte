<script lang="ts">
  import { useNavigate } from "svelte-navigator";
  import { Form, FormGroup, Input, Button } from "sveltestrap";
  import { userStore } from "../../stores/user";
  import { sendContactForm } from "../../service/ContactService";
  const navigate = useNavigate();

  let errorMessage: string|null = null;

  let mail = $userStore?.email;
  let subject = "";
  let message = "";

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    if(mail == "" || subject == "" || message == "") {
      errorMessage = "Veuillez renseigner tous les champs";
      return;
    }

    sendContactForm(mail!, subject, message).then(value => {
      if(value) {
        navigate("/");
      } else {
        errorMessage = "Erreur lors de l'envoi du message";
      }
    });
  }
    
</script>

<h1>Contacter l'administrateur</h1>
<section id="contactForm">
  <Form on:submit={handleSubmit}>
    <FormGroup floating label="Votre email">
      {#if $userStore}
        <Input type="email" name="email" value={mail} disabled required />
      {:else}
        <Input type="email" name="email" bind:value={mail} required />
      {/if}
    </FormGroup>
    <FormGroup floating label="Sujet">
      <Input id="subject" name="subject" bind:value={subject} required />
    </FormGroup>
    <FormGroup floating label="Ecrivez votre message ici...">
      <Input type="textarea" name="message" bind:value={message} required />
    </FormGroup>

    {#if errorMessage != null}
      <p id="errorMessage">{errorMessage}</p>
    {/if}

    <Button type="submit" color="primary" class="w-75">Envoyer</Button>
  </Form>
</section>

<style>
  #contactForm {
    margin: 5rem auto;
    width: 35rem;
  }
</style>
