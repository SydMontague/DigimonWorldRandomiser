package de.phoenixstaffel.dmw.randomiser;

import java.util.Random;

import de.phoenixstaffel.dmw.DigimonWorldAPI;
import de.phoenixstaffel.dmw.api.Move;
import de.phoenixstaffel.dmw.api.enums.StatusEffect;

public class MoveRandomiser {
    private Random rng;
    private DigimonWorldAPI world;
    
    public MoveRandomiser(DigimonWorldAPI world, Random rng) {
        this.rng = rng;
        this.world = world;
    }
    
    
    /*
     * MP/HP factor -> max(0.30, gaussian) + 0.2
     * damage = gaussian * 800 + 50
     * 
     * equal chance for each status effect or none (20%)
     * 5-30% for status effect
     * 
     * block factor = 50 + 50 * gaussian
     */
    public void randomiseValues() {
        for(Move m : world.getMovesManager().getMoves())
        {
            if(m.getId() > 56)
                continue;
            
            if(m.getGeneralValues().getPower() == 0)
                continue;
            
            double factor = Math.max(0.3, rng.nextGaussian()) + 0.2;
            short power = (short) (rng.nextGaussian() * 750 + 50);
            int mp = (int) Math.min(power * factor / 3, 255);
            
            m.getGeneralValues().setPower(power);
            m.getGeneralValues().setMPUsage((byte) mp);
            
            m.getGeneralValues().setEffect(StatusEffect.valueOf((byte) rng.nextInt(5)));
            m.getGeneralValues().setEffectChance((byte) (rng.nextInt(25) + 5));
            m.getGeneralValues().setBlockingFactor((byte) (50 + 50 * rng.nextGaussian()));
        }
    }
    
    public void randomiseLearningChances() {
        
    }
}
