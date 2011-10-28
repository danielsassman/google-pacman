package jp.or.iidukat.example.pacman.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.or.iidukat.example.pacman.Direction;
import jp.or.iidukat.example.pacman.PacmanGame;
import jp.or.iidukat.example.pacman.entity.CutsceneActor.Cutscene.Move;
import jp.or.iidukat.example.pacman.entity.CutsceneActor.Cutscene.StartPoint;
import android.graphics.Bitmap;

public class CutscenePacman extends CutsceneActor {

    private static enum Mode {
        ORDINARY, BIG;
    }

    private static class Sequence extends Cutscene.Sequence {
        private Mode mode;
        
        Sequence(Move move, Mode mode) {
            super(move);
            this.mode = mode;
        }
        
        Sequence(Move move) {
            this(move, Mode.ORDINARY);
        }
        
    }
    
    private static final Map<Integer, Cutscene> CUTSCENES;
    static {
        Map<Integer, Cutscene> css = new HashMap<Integer, Cutscene>();
        css.put(
            Integer.valueOf(1),
            new Cutscene(
                new StartPoint(64, 9),
                new Sequence[] {
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.75f * 0.8f * 2)),
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 32)),
                    new Sequence(
                        new Cutscene.Move(Direction.RIGHT, 0.75f * 0.8f * 2),
                        Mode.BIG),
                }));
        css.put(
            Integer.valueOf(2),
            new Cutscene(
                new StartPoint(64, 9),
                new Sequence[] {
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.75f * 0.8f * 2)),
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.75f * 0.8f * 2)),
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.75f * 0.8f * 2)),
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.75f * 0.8f * 2)),
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.75f * 0.8f * 2)),
                }));
        css.put(
            Integer.valueOf(3),
            new Cutscene(
                new StartPoint(64, 9),
                new Sequence[] {
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0.78f * 0.8f * 2)),
                    new Sequence(
                        new Cutscene.Move(Direction.LEFT, 0))
                }));
        CUTSCENES = Collections.unmodifiableMap(css);
    }

    private Mode mode;
    
    CutscenePacman(Bitmap sourceImage, PacmanGame g) {
        super(sourceImage, g);
    }

    @Override
    Cutscene getCutscene() {
        return CUTSCENES.get(Integer.valueOf(game.getCutsceneId()));
    }

    @Override
    void setMode(Cutscene cutscene) {
        Sequence s =
            (CutscenePacman.Sequence) cutscene.sequences[game.getCutsceneSequenceId()]; 
        mode = s.mode;
    }
    
    // Pacman, Ms.Pacman表示画像決定(アニメーション対応)
    @Override
    int[] getImagePos() {
        int b = 0;
        int c = 0;
        Direction d = this.getDir();
        if (Mode.BIG == mode) {
            b = 14;
            c = 0;
            int t = (int) (Math.floor(game.getGlobalTime() * 0.2) % 4);
            if (t == 3) t = 1;
            c += 2 * t;
            // BigPacMan
            Appearance a = getAppearance();
            a.setWidth(32);
            a.setHeight(32);
            a.setTopOffset(-20);
            a.setLeftOffset(-20);
            a.setOrder(111);
        } else {
            switch (d) {
            case LEFT:
                c = 0;
                break;
            case RIGHT:
                c = 1;
                break;
            case UP:
                c = 2;
                break;
            case DOWN:
                c = 3;
                break;
            }
            b = (int) (Math.floor(game.getGlobalTime() * 0.3) % 4);
            if (b == 3 && getDir() == Direction.NONE) b = 0;
            if (b == 2) b = 0;
            if (b == 3) {
                b = 2;
                c = 0;
            }
        }
        return new int[] { c, b };
    }
}
