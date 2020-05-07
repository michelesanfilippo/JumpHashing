## JumpHashing

### Michele Sanfilippo




Si prevede la costruzione di una classe JumpHashing che lavora attraverso due tabelle hash con due funzioni hash diverse per la gestione di numeri interi in un dizionario.



Sono state implementate:

- la ricerca di una chiave
- la cancellazione di una chiave
- l'inserimento di una chiave in modo opportuno come specificato nell'esercizio: nelle due tabelle non possono esservi duplicato (un elemento può appartenere ad una e una sola tabella). Se un elemento viene inserito in posizione libera viene inserito normalmente, se tale posizione è occupata si segue ciò che viene richiesto dall'esercizio, stando attenti nel cadere in loop(incrementando la dimensione di 1).

Il valore iniziale delle tabelle è settato a 23.

Le tabelle non verranno ridimensionate contemporaneamente dato che gli inserimenti vengono inizialmente fatti sulla prima tabella, ci aspetteremo una tabella(tabella 1) più grande dell'altra (tabella 2).

viene allegato un file di prova di input con 200 interi generati casualmente da inserire in tale dizionario.