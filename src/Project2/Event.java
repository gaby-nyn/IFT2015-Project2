package Project2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Event {
    public static final int  NAISSANCE  = 1;
    public static final int  REPRODUCTION  = 2;
    public static final int  MORT  = 3;
    int fidelite = 90;
    public Sim subject;
    public float time;
    public int type;



    public Event(int type, Sim sim, float t) {
        this.type= type;
        this.time= t;
        this.subject = sim;
    }

    public List<Event> naissance(List<Sim> population){
        type = NAISSANCE;
        List<Event> events = new ArrayList<>();
        Event death;
        Event reproduction = null;

        AgeModel am = new AgeModel();
        Random random = new Random();

        //N1  on tire une durée de vie D au hasard – enfiler nouvel événement de mort pour x, à temps t+D.
        double rdmAge = am.randomAge(random);
        Sim s = subject;
        s.setDeathTime(rdmAge+time);
        death = new Event(3,s,(float) s.getDeathTime());


        //N2  si x est une fille, alors on tire un temps d’attente A jusqu’à reproduction – enfiler nouvel événement de reproduction pour x, à temps t+A.
        if(subject.getSex() == Sim.Sex.F){
            double span = am.expectedParenthoodSpan(Sim.MIN_MATING_AGE_F, Sim.MAX_MATING_AGE_F);
            reproduction = new Event(2,s,(float)span + time);
        }

        //N3  on enregistre x dans la population
        population.add(s);

        //Ajouts events
        events.add(death);
        if(reproduction != null){
            events.add(reproduction);
        }

        return events;
    }
    //Lors de la mort de sim x à temps  t, on le retire de la population.
    public void mort(List<Sim> population){
        type = MORT;
        population.remove(subject);
    }

    public Event reproduction(Sim mere, List<Sim> population){
        type = REPRODUCTION;
        Event e;
        AgeModel am = new AgeModel();


        //R1 si x est morte, alors rien à faire
        if (mere.getDeathTime() < time){
            return null;
        }

        //R2 si x est d’age de reproduction (sinon, juste passer à r3), alors choisir un partenaire y pour avoir un bébé avec
        if(mere.isMatingAge(time)){
            Sim pere = choisirPere(mere, population);
            //créer le sim bébé avec sexe au hasard, temps de naissance t, et enfiler l’événement de sa naissance
            Sim bebe = new Sim(mere,pere,time,Sim.Sex.values()[new Random().nextInt(Sim.Sex.values().length)]);
            e = new Event(NAISSANCE,bebe,time);
        }
        else {
            //R3 [r3] on tire un nouveau temps d’attente A jusqu’à reproduction –
            // enfiler nouvel événement de reproduction pour x, à temps t+A (également si elle est d’âge ou non)
            double span = am.expectedParenthoodSpan(Sim.MIN_MATING_AGE_F, Sim.MAX_MATING_AGE_F);
            e = new Event(2,mere,(float)span + time);
        }
        return e;
    }

    private Sim choisirPere(Sim x,List<Sim> population) {
        Random RND = new Random(); // générateur de nombres pseudoaléatoires
        Sim y = null; // choisir pere y
        if (!x.isInARelationship(time) || RND.nextDouble()>fidelite)
        { // partenaire au hasard
            do
            {
                Sim z = population.get(new Random().nextInt(population.size()));
                if (z.getSex()!=x.getSex() && z.isMatingAge(time)) // isMatingAge() vérifie si z est de l'age acceptable
                {
                    if (x.isInARelationship(time) // z accepte si x est infidèle
                            || !z.isInARelationship(time)
                            || RND.nextDouble()>fidelite)
                    {  y = z; x.setMate(y);}
                }
            } while (y==null);
        } else
        {
            y = x.getMate();
        }
        return y;
    }

}
