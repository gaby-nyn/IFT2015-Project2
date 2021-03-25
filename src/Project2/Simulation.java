package Project2;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    public PQ eventQ;
    public static final int  NAISSANCE  = 1;
    public static final int  REPRODUCTION  = 2;
    public static final int  MORT  = 3;
    List<Sim> population = new ArrayList<>();
    public Simulation(int n, double Tmax){
        eventQ = new PQ(50); // file de priorité
        for (int i=0; i<n; i++)
        {
            Sim fondateur = new Sim(Sim.Sex.values()[new Random().nextInt(Sim.Sex.values().length)]); // sexe au hasard, naissance à 0.0
            Event E = new Event(1,fondateur, 0);
            eventQ.insert(E); // insertion dans la file de priorité
        }
        while (!eventQ.isEmpty())
        {
            Event E = eventQ.deleteMin(); // prochain événement
            if (E.time > Tmax) break; // arrêter à Tmax
            if(E.type == NAISSANCE){
                List<Event> evs =  E.naissance(population);
                for(Event ev : evs){
                        Event e = new Event(ev.type,ev.subject,ev.time);
                        eventQ.insert(e);
                }
            }
            else if(E.type == REPRODUCTION){
                Event ev = E.reproduction(E.subject,population);
                eventQ.insert(ev);
            }
            else {
                E.mort(population);
            }
        }
    }
}
