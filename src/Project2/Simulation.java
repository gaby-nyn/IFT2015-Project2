package Project2;


public class Simulation {
    public Simulation(int n, double Tmax){
        PQ eventQ = new PQ(8); // file de priorité
        for (int i=0; i<n; i++)
        {
            Sim fondateur = new Sim(Sim.Sex.M); // sexe au hasard, naissance à 0.0
            Event E = new Event();
            E.naissance(fondateur,0);
            eventQ.insert(E); // insertion dans la file de priorité
        }
        while (!eventQ.isEmpty())
        {
            Event E = eventQ.deleteMin(); // prochain événement
            if (E.time > Tmax) break; // arrêter à Tmax
            if (E.subject.getDeathTime() > E.time)
            {
               // traiter événement E
            }
        }
    }
}
