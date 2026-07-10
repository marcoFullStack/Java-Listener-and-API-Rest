function toggleToken(button, inputId) {
    const tokenInput = document.getElementById(inputId);


    const iconEye = '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>';


    const iconMonkey = '\uD83D\uDE48';

    if (tokenInput.type === 'password') {
        tokenInput.type = 'text';
        button.innerHTML = iconMonkey;
    } else {
        tokenInput.type = 'password';
        button.innerHTML = iconEye;
    }
}

document.addEventListener('visibilitychange', function() {

    if (document.visibilityState === 'visible') {

        const ticketCookie = getCookie("ID_TICKET_ODOO"); // Usa el nombre de tu cookie

        if (ticketCookie) {
            console.log("Detectado ticket en cookie al volver: " + ticketCookie);

            location.reload();

            miFuncionDeCargaDeTicket(ticketCookie);
        }
    }
});

function getCookie(name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for(let i=0;i < ca.length;i++) {
        let c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}