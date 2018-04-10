package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends Movable {

    private boolean knifeEquipped;

    public Player(){}

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    /***
     * Methods which add the various sets of images to the player resulting the various animations
     */
    public void knifeAnimation() {
        setSpriteIdle("/resources/Top_Down_Survivor/knife/idle/survivor-idle_knife_", ".png", 20);
        setSpriteMoving("/resources/Top_Down_Survivor/knife/move/survivor-move_knife_", ".png", 20);
        setSpriteMelee("/resources/Top_Down_Survivor/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15);
        knifeEquipped = true;
    }

    public void handgunAnimation() {
        setSpriteIdle("/resources/Top_Down_Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20);
        setSpriteMoving("/resources/Top_Down_Survivor/handgun/move/survivor-move_handgun_", ".png", 20);
        setSpriteMelee("/resources/Top_Down_Survivor/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15);
        setSpriteShooting("/resources/Top_Down_Survivor/handgun/shoot/survivor-shoot_handgun_", ".png", 3);
        setSpriteReloading("/resources/Top_Down_Survivor/handgun/reload/survivor-reload_handgun_", ".png", 15);
        knifeEquipped = false;
    }

    public void rifleAnimation() {
        setSpriteIdle("/resources/Top_Down_Survivor/rifle/idle/survivor-idle_rifle_", ".png", 20);
        setSpriteMoving("/resources/Top_Down_Survivor/rifle/move/survivor-move_rifle_", ".png", 20);
        setSpriteMelee("/resources/Top_Down_Survivor/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15);
        setSpriteShooting("/resources/Top_Down_Survivor/rifle/shoot/survivor-shoot_rifle_", ".png", 3);
        setSpriteReloading("/resources/Top_Down_Survivor/rifle/reload/survivor-reload_rifle_", ".png", 20);
        knifeEquipped = false;
    }

    /***
     * Method for turning player keyboard input into movement on the screen
     * @param keyEvent
     */
    public void movePlayer(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            setMoving();
            goLeft();
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            setMoving();
            goRight();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            setMoving();
            goUp();
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
            setMoving();
            goDown();
        }

        if (keyEvent.getCode() == KeyCode.E) {
            setMelee();
            System.out.println("Melee!");
        }

        if (keyEvent.getCode() == KeyCode.SPACE) {
            if(!knifeEquipped) {
                setShooting();
                System.out.println("Fire!");
            } else {
                setMelee();
                System.out.println("Melee by space!");
            }
        }

        if (keyEvent.getCode() == KeyCode.R && !knifeEquipped) {
            setReloading();
            System.out.println("Reload!");
        }
    }

    /***
     * Method for stopping movement upon key released
     * @param keyEvent
     */
    public void releasedPlayer(KeyEvent keyEvent){
        setIdle();
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT
                || keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.D) {
            stopX();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN
                || keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.S) {
            stopY();
        }
    }
}