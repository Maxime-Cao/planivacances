import { instance } from "./ApiClient";

export async function sendContactForm(mail: string, subject: string, message: string): Promise<boolean> {
    try {
        const response = await instance.post<string>("/users/admin/message", {
            email: mail,
            subject: subject,
            message: message,
          });
  
        if (response.status == 200) {
            console.log("Message envoyé avec succès");
            return true;
        } else {
            console.error("Erreur lors de l'envoi du message");
            return false;
        }
    } catch (error: any) {
        console.error("Erreur lors de l'envoi du message");
        return false;
    }
}