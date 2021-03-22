package Project2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Event {
    int fidelite = 90;
    public Sim subject;
    public float time;

    List<Sim> population = new ArrayList<>();

    public void naissance(Sim newSim, float t){
        subject = newSim;
        time = t;

        AgeModel am = new AgeModel();
        Random random = new Random();

        //N1
        double rdmAge = am.randomAge(random);
        subject.setDeathTime(rdmAge+t);
        // enfiler nouvel événement de mort pour x, à temps t+D.

        //N2  si x est une fille, alors on tire un temps d’attente A jusqu’à reproduction – enfiler nouvel événement de reproduction pour x, à temps t+A.
        if(subject.getSex() == Sim.Sex.F){

        }

        //N3 on enregistre x dans la population
        population.add(subject);

    }
    //Lors de la mort de sim x à temps  t, on le retire de la population.
    public void mort(Sim sim){
        population.remove(sim);
    }

    public void reproduction(Sim mere, float t){
        //R1 si x est morte, alors rien à faire
        if (mere.getDeathTime() < t){
            return;
        }

        //R2 si x est d’age de reproduction (sinon, juste passer à r3), alors choisir un partenaire y pour avoir un bébé avec
        if(mere.isMatingAge(t)){
            Sim pere = choisirPere(mere, t);
            //créer le sim bébé avec sexe au hasard, temps de naissance t, et enfiler l’événement de sa naissance
            Sim s = new Sim(mere,pere,t,Sim.Sex.values()[new Random().nextInt(Sim.Sex.values().length)]);

        }
        //R3 [r3] on tire un nouveau temps d’attente A jusqu’à reproduction –
        // enfiler nouvel événement de reproduction pour x, à temps t+A (également si elle est d’âge ou non)

    }

    private Sim choisirPere(Sim x, float t) {
        Random RND = new Random(); // générateur de nombres pseudoaléatoires
        Sim y = null; // choisir pere y
        if (!x.isInARelationship(t) || RND.nextDouble()>fidelite)
        { // partenaire au hasard
            do
            {
                Sim z = population.get(new Random().nextInt(population.size()));
                if (z.getSex()!=x.getSex() && z.isMatingAge(t)) // isMatingAge() vérifie si z est de l'age acceptable
                {
                    if (x.isInARelationship(t) // z accepte si x est infidèle
                            || !z.isInARelationship(t)
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
