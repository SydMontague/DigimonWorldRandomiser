package de.phoenixstaffel.dmw.randomiser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import de.phoenixstaffel.dmw.DigimonWorldAPI;
import de.phoenixstaffel.dmw.api.Digimon;
import de.phoenixstaffel.dmw.api.enums.DigimonLevel;
import de.phoenixstaffel.dmw.api.enums.Stats;

/**
 * Those who enter these halls shall see nothing but despair as hope has abandoned this place.
 * No, honestly - this code is certainly not nice but it <b>works</b> somehow.
 * I tried to refactor it once, but it's too specialised to be worth my time right now...
 */
public class EvolutionsRandomiser {
    
    private Random rng;
    private DigimonWorldAPI world;
    
    public EvolutionsRandomiser(DigimonWorldAPI world, Random rng) {
        this.rng = rng;
        this.world = world;
    }
    
    public void randomiseTree() {
        System.out.println("Randomise Tree");
        randomiseBabyToChild();
        randomiseChildToAdult();
        randomiseAdultToPerfect();
    }
    
    private void randomiseAdultToPerfect() {
        int evolutions = 52 + rng.nextInt(11);
        
        Map<Digimon, List<Digimon>> targetMap = new HashMap<>();
        Map<Digimon, List<Digimon>> fromMap = new HashMap<>();
        
        List<Digimon> tmpListFrom = new LinkedList<>();
        for (Digimon digimon : world.getDigimonManager().getDigimonByLevel(DigimonLevel.CHAMPION)) {
            if (digimon.getId() >= 62 || digimon.getId() <= 0)
                continue;
            
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            
            targetMap.put(digimon, new LinkedList<>());
        }
        
        List<Digimon> tmpListTo = new LinkedList<>();
        for (Digimon digimon : world.getDigimonManager().getDigimonByLevel(DigimonLevel.ULTIMATE)) {
            if (digimon.getId() >= 62 || digimon.getId() <= 0)
                continue;
            
            switch (digimon.getGeneralValues().getName()) {
                case "Vademon":
                    continue;
            }
            
            tmpListTo.add(digimon);
            tmpListTo.add(digimon);
            tmpListTo.add(digimon);
            tmpListTo.add(digimon);
            
            fromMap.put(digimon, new LinkedList<>());
            
            int rngRoll = rng.nextInt(tmpListFrom.size());
            Digimon chosen = tmpListFrom.get(rngRoll);
            fromMap.get(digimon).add(chosen);
            targetMap.get(chosen).add(digimon);
            tmpListFrom.remove(rngRoll);
            evolutions--;
        }
        
        for (Digimon digimon : world.getDigimonManager().getDigimonByLevel(DigimonLevel.CHAMPION)) {
            if (digimon.getId() >= 62 || digimon.getId() <= 0)
                continue;
            
            if (!targetMap.get(digimon).isEmpty())
                continue;
            
            int rngRoll = rng.nextInt(tmpListTo.size());
            Digimon chosen;
            
            while (targetMap.get(digimon).contains(chosen = tmpListTo.get(rngRoll)))
                rngRoll = rng.nextInt(tmpListTo.size());
            
            fromMap.get(chosen).add(digimon);
            targetMap.get(digimon).add(chosen);
            tmpListFrom.remove(digimon);
            tmpListTo.remove(rngRoll);
            evolutions--;
        }
        
        for (int i = 0; i < evolutions; i++) {
            int rngRoll = rng.nextInt(tmpListFrom.size());
            int rngRoll2 = rng.nextInt(tmpListTo.size());
            
            Digimon chosen = tmpListFrom.get(rngRoll);
            Digimon digimon = tmpListTo.get(rngRoll2);
            
            if (fromMap.get(digimon).contains(chosen) || targetMap.get(chosen).contains(digimon)) {
                i--;
                continue;
            }
            
            fromMap.get(digimon).add(chosen);
            targetMap.get(chosen).add(digimon);
            tmpListFrom.remove(rngRoll);
            tmpListTo.remove(rngRoll2);
        }
        
        for (Entry<Digimon, List<Digimon>> entry : targetMap.entrySet())
            entry.getKey().getEvolutionPaths().setEvolveTo(createToArray(entry.getValue()));
        
        for (Entry<Digimon, List<Digimon>> entry : fromMap.entrySet())
            entry.getKey().getEvolutionPaths().setEvolveFrom(createFromArray(entry.getValue()));
    }
    
    private void randomiseChildToAdult() {
        int evolutions = 40 + rng.nextInt(11);
        
        Map<Digimon, List<Digimon>> targetMap = new HashMap<>();
        Map<Digimon, List<Digimon>> fromMap = new HashMap<>();
        
        List<Digimon> tmpListFrom = new LinkedList<>();
        for (Digimon digimon : world.getDigimonManager().getDigimonByLevel(DigimonLevel.ROOKIE)) {
            if (digimon.getId() >= 62 || digimon.getId() <= 0)
                continue;
            
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            tmpListFrom.add(digimon);
            
            targetMap.put(digimon, new LinkedList<>());
        }
        
        List<Digimon> tmpListTo = new LinkedList<>();
        for (Digimon digimon : world.getDigimonManager().getDigimonByLevel(DigimonLevel.CHAMPION)) {
            if (digimon.getId() >= 62 || digimon.getId() <= 0)
                continue;
            switch (digimon.getGeneralValues().getName()) {
                case "Devimon":
                case "Nanimon":
                case "Numemon":
                case "Sukamon":
                    continue;
            }
            
            tmpListTo.add(digimon);
            tmpListTo.add(digimon);
            
            fromMap.put(digimon, new LinkedList<>());
            
            int rngRoll = rng.nextInt(tmpListFrom.size());
            Digimon chosen = tmpListFrom.get(rngRoll);
            fromMap.get(digimon).add(chosen);
            targetMap.get(chosen).add(digimon);
            tmpListFrom.remove(rngRoll);
        }
        
        for (int i = 0; i < evolutions - 25; i++) {
            int rngRoll = rng.nextInt(tmpListFrom.size());
            int rngRoll2 = rng.nextInt(tmpListTo.size());
            
            Digimon chosen = tmpListFrom.get(rngRoll);
            Digimon digimon = tmpListTo.get(rngRoll2);
            
            if (fromMap.get(digimon).contains(chosen) || targetMap.get(chosen).contains(digimon)) {
                i--;
                continue;
            }
            
            fromMap.get(digimon).add(chosen);
            targetMap.get(chosen).add(digimon);
            tmpListFrom.remove(rngRoll);
            tmpListTo.remove(rngRoll2);
        }
        
        for (Entry<Digimon, List<Digimon>> entry : targetMap.entrySet())
            entry.getKey().getEvolutionPaths().setEvolveTo(createToArray(entry.getValue()));
        
        for (Entry<Digimon, List<Digimon>> entry : fromMap.entrySet())
            entry.getKey().getEvolutionPaths().setEvolveFrom(createFromArray(entry.getValue()));
    }
    
    private void randomiseBabyToChild() {
        List<Digimon> tmpList = new LinkedList<>();
        for (Digimon d : world.getDigimonManager().getDigimonByLevel(DigimonLevel.IN_TRAINING)) {
            tmpList.add(d);
            tmpList.add(d);
            d.getEvolutionPaths().setEvolveTo(new byte[] { -1, -1, -1, -1, -1, -1 });
        }
        
        for (Digimon digimon : world.getDigimonManager().getDigimonByLevel(DigimonLevel.ROOKIE)) {
            if (digimon.getGeneralValues().getName().equals("Kunemon") || digimon.getId() >= 62 || digimon.getId() <= 0)
                continue;
            
            int rngRoll = rng.nextInt(tmpList.size());
            Digimon chosen = tmpList.get(rngRoll);
            tmpList.remove(rngRoll);
            
            Digimon[] arr = new Digimon[5];
            arr[2] = chosen;
            digimon.getEvolutionPaths().setEvolveFrom(arr);
            
            arr = chosen.getEvolutionPaths().getEvolveTo();
            if (arr[2] == null)
                arr[2] = digimon;
            else
                arr[3] = digimon;
            
            chosen.getEvolutionPaths().setEvolveTo(arr);
        }
    }
    
    /*
     * General: clear out old requirements!
     */
    public void randomiseRequirements() {
        System.out.println("Randomise Requirements");
        
        for (Digimon digimon : world.getDigimonManager().getDigimons()) {
            if (digimon.getId() <= 0 || digimon.getId() >= 62)
                continue;
            
            switch (digimon.getGeneralValues().getName()) {
                case "Vademon":
                case "Devimon":
                case "Nanimon":
                case "Numemon":
                case "Sukamon":
                case "Kunemon":
                    continue;
            }
            
            digimon.getEvolutionRequirements().setBonusDigimon((short) -1);
            
            digimon.getEvolutionRequirements().setHP((short) -1);
            digimon.getEvolutionRequirements().setMP((short) -1);
            digimon.getEvolutionRequirements().setOffense((short) -1);
            digimon.getEvolutionRequirements().setDefense((short) -1);
            digimon.getEvolutionRequirements().setSpeed((short) -1);
            digimon.getEvolutionRequirements().setBrains((short) -1);
            
            switch (digimon.getGeneralValues().getDigimonLevel()) {
                case ROOKIE:
                    randomiseChildRequirements(digimon);
                    break;
                case CHAMPION:
                    randomiseChampionRequirements(digimon);
                    break;
                case ULTIMATE:
                    randomisePerfectRequirements(digimon);
                    break;
                default:
                    continue;
            }
        }
    }
    
    /*
     * to Child
     * - set type bonus requirement
     * - select 3 random stats
     * - set them to 10/1
     * - keep rest unchanged
     * - (make sure the 3 stats are not 100% the same for the same tree)
     */
    private void randomiseChildRequirements(Digimon digimon) {
        digimon.getEvolutionRequirements().setBonusDigimon(digimon.getEvolutionPaths().getEvolveFrom()[2]);
        
        List<Stats> select = new ArrayList<>(Arrays.asList(Stats.values()));
        for (int i = 0; i < 3; i++) {
            int rand = rng.nextInt(select.size());
            digimon.getEvolutionRequirements().setStats(select.get(rand), (short) 1);
            select.remove(rand);
        }
    }
    
    /*
     * to Adult
     * - select 1 random bonus requirement (Digimon, Happiness [50-90], Discipline [50-90], Battles [-10 - 10])
     * - generate random weight requirement (20 - 40)
     * - generate random care mistake requirement (-5 - 5)
     * - pick 1-4 stats (35%, 35%, 15%, 15%), set them to 100
     */
    private void randomiseChampionRequirements(Digimon digimon) {
        digimon.getEvolutionRequirements().setBattles((short) -1);
        digimon.getEvolutionRequirements().setCare((short) 0);
        digimon.getEvolutionRequirements().setHappiness((short) -1);
        digimon.getEvolutionRequirements().setDiscipline((short) -1);
        digimon.getEvolutionRequirements().setFlags((short) 0);
        digimon.getEvolutionRequirements().setTechs((short) 0);
        
        switch (rng.nextInt(4)) {
            case 0: // add Digimon requirement
                List<Digimon> from = new ArrayList<>();
                for (Digimon d : digimon.getEvolutionPaths().getEvolveFrom())
                    if (d != null)
                        from.add(d);
                
                digimon.getEvolutionRequirements().setBonusDigimon(from.get(rng.nextInt(from.size())));
                break;
            case 1: // add discipline requirement
                digimon.getEvolutionRequirements().setDiscipline((short) (rng.nextInt(8) * 5 + 50));
                break;
            case 2: // add happiness requirement
                digimon.getEvolutionRequirements().setHappiness((short) (rng.nextInt(8) * 5 + 50));
                break;
            case 3: // add battles requirement
                int battles = rng.nextInt(21) - 10;
                if (battles > 0)
                    digimon.getEvolutionRequirements().setMaxBattles(true);
                digimon.getEvolutionRequirements().setBattles((short) Math.abs(battles));
                break;
        }
        
        digimon.getEvolutionRequirements().setWeight((short) (rng.nextInt(4) * 5 + 20));
        
        int care = rng.nextInt(6) - 5;
        if (care > 0)
            digimon.getEvolutionRequirements().setMaxCareMistakes(true);
        digimon.getEvolutionRequirements().setCare((short) Math.abs(care));
        
        digimon.getEvolutionRequirements().setTechs((short) (18 + rng.nextInt(32)));
        
        List<Stats> select = new ArrayList<>(Arrays.asList(Stats.values()));
        
        int bound = rng.nextInt(100);
        
        if (bound < 35)
            bound = 1;
        else if (bound < 70)
            bound = 2;
        else if (bound < 85)
            bound = 3;
        else
            bound = 4;
        
        for (int i = 0; i < bound; i++) {
            int rand = rng.nextInt(select.size());
            digimon.getEvolutionRequirements().setStats(select.get(rand), (short) 100);
            select.remove(rand);
        }
    }
    
    /*
     * to Perfect
     * - select 1-2 random bonus requirements (Digimon, Happiness [50-95], Discipline [50-95], Battles [-10 - 40])
     * - generate random weight requirement (5 - 65)
     * - generate random care mistake requirement (-15 - 15)
     * - pick 4 OR 6 stats, generate random values between 300 and 500 for them
     */
    private void randomisePerfectRequirements(Digimon digimon) {
        digimon.getEvolutionRequirements().setBattles((short) -1);
        digimon.getEvolutionRequirements().setCare((short) 0);
        digimon.getEvolutionRequirements().setHappiness((short) -1);
        digimon.getEvolutionRequirements().setDiscipline((short) -1);
        digimon.getEvolutionRequirements().setFlags((short) 0);
        digimon.getEvolutionRequirements().setTechs((short) 0);
        
        List<Integer> list = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        int bound = rng.nextBoolean() ? 1 : 2;
        for (int i = 0; i < bound; i++) {
            int value = rng.nextInt(list.size());
            switch (list.get(value)) {
                case 0: // add Digimon requirement
                    List<Digimon> from = new ArrayList<>();
                    for (Digimon d : digimon.getEvolutionPaths().getEvolveFrom())
                        if (d != null)
                            from.add(d);
                    
                    digimon.getEvolutionRequirements().setBonusDigimon(from.get(rng.nextInt(from.size())));
                    break;
                case 1: // add discipline requirement
                    digimon.getEvolutionRequirements().setDiscipline((short) (rng.nextInt(9) * 5 + 50));
                    break;
                case 2: // add happiness requirement
                    digimon.getEvolutionRequirements().setHappiness((short) (rng.nextInt(9) * 5 + 50));
                    break;
                case 3: // add battles requirement
                    int battles = rng.nextInt(51) - 10;
                    if (battles > 0)
                        digimon.getEvolutionRequirements().setMaxBattles(true);
                    digimon.getEvolutionRequirements().setBattles((short) Math.abs(battles));
                    break;
            }
            list.remove(value);
        }
        
        digimon.getEvolutionRequirements().setWeight((short) (rng.nextInt(17) * 5 + 5));
        
        int care = rng.nextInt(16) - 15;
        if (care > 0)
            digimon.getEvolutionRequirements().setMaxCareMistakes(true);
        digimon.getEvolutionRequirements().setCare((short) Math.abs(care));
        
        digimon.getEvolutionRequirements().setTechs((short) (18 + rng.nextInt(32)));
        
        List<Stats> select = new ArrayList<>(Arrays.asList(Stats.values()));
        
        int bound2 = rng.nextBoolean() ? 4 : 6;
        
        for (int i = 0; i < bound2; i++) {
            int rand = rng.nextInt(select.size());
            digimon.getEvolutionRequirements().setStats(select.get(rand), (short) (rng.nextInt(5) * 50 + 300));
            select.remove(rand);
        }
        
    }
    
    private void randomiseStatsgainForDigimon(Digimon digimon) {
        DigimonLevel level = digimon.getGeneralValues().getDigimonLevel();
        
        int basis = getBasisByLevel(level);
        int rolls = getRollsByLevel(level);
        
        int[] array = new int[6];
        
        for (int i = 0; i < rolls; i++) {
            int rngRoll;
            do
                rngRoll = rng.nextInt(6);
            while (array[rngRoll] >= 14);
            
            array[rngRoll]++;
        }
        
        digimon.getStatsGains().setHP((short) ((basis + array[0] * 50) * 10));
        digimon.getStatsGains().setMP((short) ((basis + array[1] * 50) * 10));
        digimon.getStatsGains().setOffense((short) ((basis + array[2] * 50)));
        digimon.getStatsGains().setDefense((short) ((basis + array[3] * 50)));
        digimon.getStatsGains().setSpeed((short) ((basis + array[4] * 50)));
        digimon.getStatsGains().setBrains((short) ((basis + array[5] * 50)));
    }
    
    private int getRollsByLevel(DigimonLevel level) {
        switch (level) {
            case ROOKIE:
                return 2;
            case CHAMPION:
                return 7 + rng.nextInt(5);
            case ULTIMATE:
                return 20 + rng.nextInt(13);
            default:
                return 0;
        }
    }
    
    private int getBasisByLevel(DigimonLevel level) {
        switch (level) {
            case ROOKIE:
                return 50;
            case CHAMPION:
                return 100;
            case ULTIMATE:
                return 300;
            default:
                return 0;
        }
    }
    
    public void randomiseStatsgains() {
        System.out.println("Randomise Statsgains");
        for (Digimon digimon : world.getDigimonManager().getDigimons()) {
            if (digimon.getId() <= 0 || digimon.getId() >= 62)
                continue;
            
            randomiseStatsgainForDigimon(digimon);
        }
    }
    
    private Digimon[] createToArray(List<Digimon> list) {
        switch (list.size()) {
            case 6:
                return new Digimon[] { list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5) };
            case 5:
                return new Digimon[] { list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), null };
            case 4:
                return new Digimon[] { null, list.get(0), list.get(1), list.get(2), list.get(3), null };
            case 3:
                return new Digimon[] { null, list.get(0), list.get(1), list.get(2), null, null };
            case 2:
                return new Digimon[] { null, null, list.get(0), list.get(1), null, null };
            case 1:
                return new Digimon[] { null, null, list.get(0), null, null, null };
            default:
                System.out.println("Should not happen! Size was " + list.size());
                return new Digimon[] { null, null, null, null, null, null };
        }
    }
    
    private Digimon[] createFromArray(List<Digimon> list) {
        
        switch (list.size()) {
            case 1:
                return new Digimon[] { null, null, list.get(0), null, null };
            case 2:
                return new Digimon[] { null, list.get(0), list.get(1), null, null };
            case 3:
                return new Digimon[] { null, list.get(0), list.get(1), list.get(2), null };
            case 4:
                return new Digimon[] { list.get(0), list.get(1), list.get(2), list.get(3), null };
            case 5:
                return new Digimon[] { list.get(0), list.get(1), list.get(2), list.get(3), list.get(4) };
            default:
                System.out.println("Should not happen! Size was " + list.size());
                return new Digimon[] { null, null, null, null, null };
        }
    }
}
