package Project2;


import java.util.*;

public class Simulation {
    public PQ eventQ = new PQ(50);
    public static final int  NAISSANCE  = 1;
    public static final int  REPRODUCTION  = 2;
    public static final int  MORT  = 3;
    List<Sim> population = new ArrayList<>();

    public static int echantillonSimulation = 100;
    public static PriorityQueue<Sim> populationPQ;
    public static Map<Integer,Sim> maleAncestorMap;
    public static Map<Integer,Sim> femaleAncestorMap;
    public static Map<Integer, PointCoalescence> aieuxLigneeMap;
    public static Map<Integer, PointCoalescence> aieuleLigneeMap;

    public Simulation(int n, double Tmax){
        for (int i=0; i<n; i++)
        {
            Sim fondateur = new Sim(Sim.Sex.values()[new Random().nextInt(Sim.Sex.values().length)]); // sexe au hasard, naissance à 0.0
            Event E = new Event(1,fondateur, 0);
            eventQ.insert(E); // insertion dans la file de priorité
        }
        while (!eventQ.isEmpty())
        {
            Event E = eventQ.deleteMin(); // prochain événement
            if (E.time > Tmax) {
                break; // arrêter à Tmax
            }
            else if (E.time >= echantillonSimulation) {
                echantillonSimulation += 100;
                populationPQ = new PriorityQueue<>();
                maleAncestorMap = new HashMap<>();
                femaleAncestorMap = new HashMap<>();
                aieuxLigneeMap = new HashMap<>();
                aieuleLigneeMap = new HashMap<>();
                for (Sim sim : population) {
                    populationPQ.add(sim);
                }
                while(!populationPQ.isEmpty()) {
                    Sim youngestSim = removeYoungest();
                    Sim father = youngestSim.getFather();
                    Sim mother = youngestSim.getMother();
                    if (!youngestSim.isFounder()) {
                        if (!maleAncestorMap.isEmpty()) {
                            if (verifyAncestor(father)) {
                                if (aieuxLigneeMap.get(father.getSim_ident()) == null) {
                                    aieuxLigneeMap.put(father.getSim_ident(), new PointCoalescence(youngestSim.getBirthTime(), father.getPA_Size()));
                                }
                            }
                        } else {
                            maleAncestorMap.put(father.getSim_ident(), father);
                        }
                        if (!femaleAncestorMap.isEmpty()) {
                            if (verifyAncestor(mother)) {
                                if (aieuleLigneeMap.get(mother.getSim_ident()) == null) {
                                    aieuleLigneeMap.put(mother.getSim_ident(), new PointCoalescence(youngestSim.getBirthTime(), mother.getPA_Size()));
                                }
                            }
                        } else {
                            femaleAncestorMap.put(mother.getSim_ident(), mother);
                        }
                    }
                }
                System.out.println("Temps: " + E.time + " Taille population: " + population.size());
                System.out.println("Temps: " + E.time + " Nombre de lignées paternel: " + aieuxLigneeMap.size());
                System.out.println("Temps: " + E.time + " Nombre de lignées maternel: " + aieuleLigneeMap.size());
            }

            if (E.type == NAISSANCE){
                List<Event> evs =  E.naissance(population);
                for (Event ev : evs){
                        eventQ.insert(ev);
                }
            }
            else if (E.type == REPRODUCTION){
                Event ev = E.reproduction(E.subject,population);
                if (ev != null) {
                    eventQ.insert(ev);
                }
            }
            else if (E.type == MORT){
                E.mort(population);
            }
            if (population.size() <= 0) break;
        }
    }

    private Sim removeYoungest() {
        return populationPQ.remove();
    }

    private boolean verifyAncestor(Sim ancestor) {
        if (ancestor.getSex() == Sim.Sex.M) {
            if (maleAncestorMap.get(ancestor.getSim_ident()) != null) {
                return true;
            }
        }
        else if (ancestor.getSex() == Sim.Sex.F) {
            if (femaleAncestorMap.get(ancestor.getSim_ident()) != null) {
                return true;
            }
        }
        return false;
    }

    private class PointCoalescence {
        private double birthDate = 0;
        private int nbAllele = 0;

        public PointCoalescence(double birthDate, int nbAllele) {
            this.birthDate = birthDate;
            this.nbAllele = nbAllele;
        }

        public double getBirthDate() {
            return birthDate;
        }

        public int getNbAllele() {
            return nbAllele;
        }
    }
}
