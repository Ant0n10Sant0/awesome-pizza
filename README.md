# Awesome Pizza API

Ho deciso di utilizzare Spring MVC (poiché era nella descrizione della posizione) per la parte di gestione delle richieste, mentre per l'interazione con il database (la cui scelta è ricaduta su PostgreSQL) ho usato Spring Data JDBC.

### API

Ho implementato un solo controller per gestire esclusivamente gli ordini, con le funzionalità descritte nella traccia dell'esercizio. In particolare, per semplicità, ho deciso di gestire anche la parte di chiusura dell'ordine tramite codice.

### Database

L'immagine sottostante rappresenta la struttura del db che ho adottato. Ho usato il versionamento e l'auditing forniti da Spring, rispettivamente attraverso il campo `rec_ver` e i campi `tsi` (timestamp di insert) e `tsu` (timestamp di update). Ho anche provato a implementare la cancellazione logica attraverso il campo `log_del`.

Avendo usato il meccanismo di inizializzazione del datasource predefinito di Spring Boot, è possibile trovare lo script di creazione del db nel file [schema.sql](src/main/resources/schema.sql).

![Schema del database `awesomepizza`](/assets/images/db.png)
