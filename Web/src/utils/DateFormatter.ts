export function formatDateForDisplay(dateString:string) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth()+1).padStart(2,'0');
    const day = String(date.getDate()).padStart(2,'0');
    return `${day}/${month}/${year}`;
}

export function formatTimestampForDisplay(timestamp:number) {
    const date = new Date(timestamp);
    const jour = date.getDate();
    const mois = date.getMonth() + 1;
    const annee = date.getFullYear();
    const heures = date.getHours();
    const minutes = date.getMinutes();

    const ajouterZero = (nombre:number) => (nombre < 10 ? `0${nombre}` : nombre);

    return `${ajouterZero(jour)}/${ajouterZero(mois)}/${annee} - ${ajouterZero(heures)}h${ajouterZero(minutes)}`;
}