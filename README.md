# petdispenser
<b> Come funziona? </b>
L'utente quando installa l'applicazione deve inserire un animale, subito dopo deve collegarsi al dispenser, e infine creare una dieta.
La dieta al suo interno ha dei pasti i quali hanno degli orari.
L'utente ogni volta che effettua una modifica di un pasto deve effettuare la sincronizzazione con il dispenser.
Inoltre l'autenticazione viene effettuata tramite google utilizzando firebase,il quale restituisce un google user id criptato
che verrà poi successivamente decriptato da firebase stesso al momento della richiesta dell'operazione(la richiesta di decriptazione la fa aws amplify)
in questo modo ogni tipo di operazione è sicura al 100%


<b>IMPORTANTE</b>: L'UTENTE PRIMA DI CHIUDERE IL DISPOSITIVO DEVE PREMERE SYNCH PERCHE' IN QUESTO PROTOTIPO IL DISPOSITIVO HA UN SOLO CONTENITORE.


