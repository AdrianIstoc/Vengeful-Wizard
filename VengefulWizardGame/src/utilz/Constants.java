package utilz;

import main.Game;

public class Constants {

    public static final float GRAVITY = 0.02f * Game.SCALE;

    public static class EnemyConstants {
        public static final int MAG=0;

        public static final int IDLE =0;
        public static final int ATTACK =1;
        public static final int DEAD =2;

        public static final int MAG_SCALE_DEFALUT = 64;
        public static final int MAG_SCALE = (int) (MAG_SCALE_DEFALUT * Game.SCALE);

        public static final int MAG_DRWOFFSET_X= (int) (16*Game.SCALE);
        public static final int MAG_DRWOFFSET_Y= (int) (18*Game.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type){
                case MAG:
                    switch (enemy_state) {
                        case IDLE:
                            return 5;
                        case ATTACK:
                        case DEAD:
                            return 6;
                    }
            }
            return 0;
        }

        public static int GetMaxHealt(int enemy_type) {
            switch (enemy_type) {
                case MAG:
                    return 3;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type){
                case MAG:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    public static class UI{
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 160;
            public static final int B_HEIGHT_DEFAULT = 32;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int PAUSE_SIZE_DEFAULT = 64;
            public static final int PAUSE_SIZE = (int) (PAUSE_SIZE_DEFAULT * Game.SCALE / 2);
        }

        public static class VolumeSlider {
            public static final int VOLUME_DEFAULT_WIDTH = 22;
            public static final int VOLUME_DEFAULT_HEIGHT = 64;
            public static final int SLIDER_DEFAULT_WIDTH = 274;
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE / 2);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE / 2);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE / 2);
        }
    }

    public static class Directions {
        public static final int LEFT=0;
        public static final int UP=1;
        public static final int RIGHT=2;
        public static final int DOWN=3;
    }
    public static class PlayerConstants{
        public static final int IDLE=0;
        public static final int WALK_RIGHT=1;
        public static final int WALK_LEFT=2;
        public static final int JUMP_RIGHT=3;
        public static final int JUMP_LEFT=4;
        public static final int SHOOT_RIGHT=5;
        public static final int SHOOT_LEFT=6;
        public static final int JUMP_SHOOT_RIGHT=7;
        public static final int JUMP_SHOOT_LEFT=8;

        public static int GetSpriteAmount(int player_action) {

            switch (player_action) {
                case IDLE:
                case JUMP_RIGHT:
                case JUMP_LEFT:
                    return 4;
                case WALK_RIGHT:
                case WALK_LEFT:
                    return 7;
                case SHOOT_RIGHT:
                case SHOOT_LEFT:
                case JUMP_SHOOT_RIGHT:
                case JUMP_SHOOT_LEFT:
                    return 2;
                default:
                    return 1;
            }
        }


    }
}
