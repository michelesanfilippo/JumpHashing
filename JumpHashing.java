/*
Seconda prova in itinere di laboratorio di algoritmi A.A. 2019/2020 prof.ssa Marinella Sciortino

Soluzione svolta da Michele Sanfilippo matricola 0664184

L'esercizio richiede la gestione di un dizionario per numeri interi attraverso le funzioni di insert,search e delete.
Richiede inoltre di implementare tale dizionario attraverso due tabelle hash che non dovranno contenere duplicati,
ovvero un elemento E in Tabella1 non può esistere in Tabella2.

*/
import java.util.*;
import java.io.*;
import java.lang.*;

//La classe principale dell'esercizio sarà JumpHashing
public class JumpHashing{

    //JumpHashing conterrà due hash tables che saranno di tipo Table
    static class Table{

        private static final int INIT_DIM = 23;//dimensione iniziale richiesta
        private int n;//coppie di chiavi - valori
        private int m;//dimensione dell'hash table
        private int[] keys;//chiavi presenti nell'hash table
        private int[] values;//valori associati alle chiavi nell'hash table

        //costruttore iniziale per inizializzare un oggetto di tipo Table con dim. iniziale 23
        public Table(){
            this(INIT_DIM);//inizializzo a INIT_DIM
        }
        //Costruttore per inizializzare un oggetto Table con dim. qualsiasi
        public Table(int dimension){
            m = dimension;
            keys = new int[m];
            values = new int[m];
            n = 0;
        }
    }
    //Attributi di un oggetto di tipo JumpHashing
    private static Table hashTable1; //La prima hash table
    private static Table hashTable2; //La seconda hash table
    private static int count; //Contatore per capire se mi trovo in un loop

    //Costruttore che inizializza le tabelle
    public JumpHashing(){
        hashTable1 = new Table();
        hashTable2 = new Table();
    }

    //Controllo se la tabella presa a parametro è vuota
    public static boolean isEmpty(Table t){
        return (t.n == 0) ? true : false;
    }

    //Prima funzione hash richiesta dall'esercizio
    public static int hashFunction1(int key, Table t1){
        return (11 * key) % t1.m;
    }

    //Seconda funzione hash richiesta dall'esercizio
    public static int hashFunction2(int key, Table t2){
        return (13 * key) % t2.m;
    }

    //Funzione di resize della tabella passata a parametro con nuova dimensione passata
    //anch'essa a parametro
    public static void resize(int dimension, Table t){
        Table temp = new Table(dimension); //Utilizzio una nuova tabella temporanea
        int newPosition; //devo calcolare la nuova posizione data la nuova dimensione
            //Scorro gli elementi della tabella
            for(int i=0; i<t.m; i++){
                //Lo 0 significa posizione vuota
                if(t.keys[i] != 0){
                    //Controllo se sono nella prima hash table o nella seconda
                    //per calcolarmi la nuova posizione
                    if(t.equals(hashTable1)){
                        newPosition = hashFunction1(t.keys[i], temp);
                    }
                    else{
                        newPosition = hashFunction2(t.keys[i], temp);
                    }
                    //In temp in nuova posizione(calcolata con la nuova dim) vado ad inserire
                    //il valore che era presente in posizione i nella tabella hash presa in considerazione
                    temp.keys[newPosition] = t.keys[i];
                    temp.values[newPosition] = t.values[i];
                }
            }
        //Faccio puntare gli arrai keys,values e m(dimensione) della tabella ai rispettivi di temp(aggiornati)
        t.keys = temp.keys;
        t.values = temp.values;
        t.m = temp.m;
        //Avrò la tabella aggiornata con i valori e le chiavi nelle nuove posizioni come richiesto da esercizio
    }

    //Funzione di ricerca che richiede tempo costante dell'ordine O(1)
    //Ciò che bisogna fare in questo esercizio è calcolare le posizioni attraverso le funzioni hash
    //e cercare la chiave in tali posizioni rispettivamente nella prima tabella e nella seconda.
    //Se in entrambe le posizioni non vi è la chiave cercata, allora tale chiave non sarà presente.
    public static boolean search(int key){
        //Calcolo le posizioni
        int h1 = hashFunction1(key, hashTable1);
        int h2 = hashFunction2(key, hashTable2);

        //Se è presente restituisco true
        if(hashTable1.keys[h1] == key) return true;
        if(hashTable2.keys[h2] == key) return true;
        return false; //Altrimenti false
    }

    //Funzione di delete che si basa sulla funzione di search, cerco un elemento e devo mettere 0 in tale 
    //posizione. Pertanto il tempo impiegato è costante O(1).
    public static boolean delete(int key){
        //Calcolo le posizioni
        int h1 = hashFunction1(key, hashTable1);
        int h2 = hashFunction2(key, hashTable2);

        //Se trovo, elimino sia la chiave che il valore, diminuisco il numero
        //di coppie e restituisco true
        if(hashTable1.keys[h1] == key){
            hashTable1.keys[h1] = 0;
            hashTable1.values[h1] = 0;
            hashTable1.n -= 1;
            return true;
        }
        if(hashTable2.keys[h2] == key){
            hashTable2.keys[h2] = 0;
            hashTable2.values[h2] = 0;
            hashTable2.n -= 1;
            return true;
        }
        return false;//Altrimenti false
    }

    //Funzione di insert che inserisce la coppia chiave valore.
    //Viene inserita tale coppia nella tabella 1 :
    // - Se la posizione calcolata è vuota inserisco
    // - Se la chiave è già presente non inserisco
    // - Se la posizione è occupata, prelevo la coppia presente e la sostituisco.
    // Tale coppia prelevata viene inserita nella tabella 2 :
    // - Se la posizione calcolata è vuota inserisco
    // - Se la chiave è già presente non inserisco
    // - Se la posizione è occupata, prelevo la coppia presente e la sostituisco.
    // La coppia dovrà essere inserita nella Tabella 1.
    // Procedo in tale modo ricorsivamente stando attento a non finire in un loop, per fare ciò
    // il mio count non dovrà superare 12(ovvero 6 sostituzioni nelle sempre nelle stesse posizioni)
    public static void insert(int key, int value){
        //Se sono in "loop",  azioni massime previste
        //aumento la taglia delle tabelle di 1 e reinserisco opportunamente
        if(count == 12){
            resize(hashTable1.m+1, hashTable1);
            resize(hashTable2.m+1, hashTable2);
        }
        //Se la condizione è vera raddoppio la taglia della tabella e reinserisco opportunamente
        if(hashTable1.n >= hashTable1.m/2) resize(hashTable1.m*2, hashTable1);
        if(hashTable2.n >= hashTable2.m/2) resize(hashTable2.m*2, hashTable2);

        //Calcolo la posizione della chiave da inserire
        int h1 = hashFunction1(key, hashTable1);
        //Se già presente mi fermo
        if(key == hashTable1.keys[h1]){
            count = 0;
            return;
        }
        //Altrimenti se la posizione è libera inserisco
        if(hashTable1.keys[h1] == 0){
            hashTable1.keys[h1] = key;
            hashTable1.values[h1] = value;
            hashTable1.n += 1; //Aumento il numero di coppie
            count = 0; //Setto il contatore a 0 perchè sicuramente non posso cadere in un loop
            return;
        }
        //Se la posizione era occupata
        else{
            //Prelevo la coppia che era presente
            int keyY = hashTable1.keys[h1];
            int valueY = hashTable1.values[h1];
            //Calcolo per tale coppia la nuova posizione nella Tabella 2
            int h2 = hashFunction2(keyY, hashTable2);
            //Sostituisco i valori
            hashTable1.keys[h1] = key;
            hashTable1.values[h1] = value;
            //non incremento n perchè ho scambiato due coppie non ne ho aggiunte
            count +=1; //Aumento il contatore perchè ho avuto uno scambio
            //Se nella Tabella 2 la chiave è presente non inserisco
            if(keyY == hashTable2.keys[h2]){
                count = 0;
                return;
            }
            //Se la posizione è libera inserisco
            if(hashTable2.keys[h2] == 0){
                hashTable2.keys[h2] = keyY;
                hashTable2.values[h2] = valueY;
                hashTable2.n += 1;
                count = 0; //Risetto il count a 0 poichè mi sono fermato
                return;
            }
            //Altrimenti se anche la tabella 2 ha la posizione già occupata
            //Scambio dinuovo senza incrementare n(numero di coppie) e chiamo ricorsivamente
            int keyZ = hashTable2.keys[h2];
            int valueZ = hashTable2.values[h2];
            hashTable2.keys[h2] = keyY;
            hashTable2.values[h2] = valueY;
            count += 1; //Aumento ancora il contatore (secondo scambio)
            insert(keyZ, valueZ);
        }
        return;
    }

    //Metodo per stampare le chiavi nelle tabella
    public static void printKeys(Table t){
        for(int i=0; i<t.keys.length; i++){
            System.out.println(i + ": " + t.keys[i]);
        }
    }

    public static void main(String[] args){
        JumpHashing jh = new JumpHashing();
        try{
            //Leggo da file di input una serie di interi
            Scanner reader = new Scanner(new FileReader("input.txt"));
            //Dove memorizzo tali interi
            int chiave;
            //Inizio a contare prima di inserire gli elementi
            double init = System.currentTimeMillis();
            while(reader.hasNextInt()){
                //inserisco gli elementi in modo opportuno attraverso insert
                chiave = reader.nextInt();
                //NOTA: il valore è 1 per tutte le chiavi dato che l'esercizio
                //richiede una ricerca per chiavi ignorando i valori
                jh.insert(chiave, 1);
                }
            //Fine degli inserimenti
            double end = System.currentTimeMillis();
            //tempo finale
            double time = end - init ;
            reader.close();
            //Controllo in modo brutale se effettivamente vi sono duplicati o meno
            boolean flag = false;
            for(int i=0; i<jh.hashTable1.keys.length; i++){
                for(int j=0; j<jh.hashTable2.keys.length; j++){
                    if(jh.hashTable1.keys[i] == jh.hashTable2.keys[j]){
                        if(jh.hashTable1.keys[i] != 0){
                            flag = true;
                            System.out.println("true, duplicato presente");
                        }
                    }
                }
            }

            System.out.println("\n");
            if(!flag) System.out.println("false, duplicati non presenti");
            System.out.println("\n");
            //Stampo le tabelle
            System.out.println("---TABELLA HASH N.1---");
            printKeys(jh.hashTable1);
            System.out.println("---TABELLA HASH N.2---");
            printKeys(jh.hashTable2);
            //Cerco 8 non presente nel mio set di elementi per testare la funzione search
            System.out.println("elemento cercato presente? " + search(8) + " possibili posizioni: Tabella 1: " + hashFunction1(8, jh.hashTable1) + " - Tabella 2: " + hashFunction2(8,jh.hashTable2));
            //Cerco 843 che è presente per testare dinuovo
            System.out.println("elemento cercato presente? " + search(843) + " possibili posizioni: Tabella 1: " + hashFunction1(843, jh.hashTable1) + " - Tabella 2: " + hashFunction2(843,jh.hashTable2));
            //Cerco 139 ancora per testare e provo la funzione di delete
            System.out.println("elemento cercato presente? " + search(139) + " possibili posizioni: Tabella 1: " + hashFunction1(139, jh.hashTable1) + " - Tabella 2: " + hashFunction2(139,jh.hashTable2));
            System.out.println("elemento eliminato? " + delete(139));
            //Adesso il search restituisce false, dato che abbiamo eliminato precedentemente il valore 139
            System.out.println("elemento cercato presente? " + search(139));
            //Stampo il tempo che ho impiegato per gli inserimenti
            System.out.println("tempo impiegato per inserimenti in ms: " + time);

        }
        catch(Exception e){
            System.out.println("ERRORE");
        }

    }
}