package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

public class Player extends Movable {

    private boolean knifeEquipped;

    public Player(){}

    public Player(String filename, String extension, int numberImages, int positionX, int positionY, int healthPoints) {
        super(filename, extension, numberImages, positionX, positionY, healthPoints);
    }

    /***
     * Methods which add the various sets of images to the player resulting the various animations
     */

    public void playerAnimation(String animationWanted) {
        if (animationWanted == "knife") {
            setSpriteIdle("/resources/Art/Survivor/knife/idle/survivor-idle_knife_", ".png", 20);
            setSpriteMoving("/resources/Art/Survivor/knife/move/survivor-move_knife_", ".png", 20);
            setSpriteMelee("/resources/Art/Survivor/knife/meleeattack/survivor-meleeattack_knife_", ".png", 15);
            knifeEquipped = true;
        } else if (animationWanted == "handgun") {
            setSpriteIdle("/resources/Art/Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20);
            setSpriteMoving("/resources/Art/Survivor/handgun/move/survivor-move_handgun_", ".png", 20);
            setSpriteMelee("/resources/Art/Survivor/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15);
            setSpriteShooting("/resources/Art/Survivor/handgun/shoot/survivor-shoot_handgun_", ".png", 3);
            setSpriteReloading("/resources/Art/Survivor/handgun/reload/survivor-reload_handgun_", ".png", 15);
            knifeEquipped = false;
        } else if (animationWanted == "rifle") {
            setSpriteIdle("/resources/Art/Survivor/rifle/idle/survivor-idle_rifle_", ".png", 20);
            setSpriteMoving("/resources/Art/Survivor/rifle/move/survivor-move_rifle_", ".png", 20);
            setSpriteMelee("/resources/Art/Survivor/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15);
            setSpriteShooting("/resources/Art/Survivor/rifle/shoot/survivor-shoot_rifle_", ".png", 3);
            setSpriteReloading("/resources/Art/Survivor/rifle/reload/survivor-reload_rifle_", ".png", 20);
            knifeEquipped = false;
        }
    }

    AudioClip audioClipFire = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/pistol_shot.wav").toExternalForm());
    AudioClip audioClipReload = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/pistol_reload.mp3").toExternalForm());
    AudioClip audioClipWalking = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/footsteps_single.wav").toExternalForm());

    /***
     * Method for turning player keyboard input into movement on the screen
     * @param keyEvent
     */
    public void movePlayer(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
            setMoving();
            goLeft();
            audioClipWalking.play();
        } else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
            setMoving();
            goRight();
            audioClipWalking.play();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
            setMoving();
            goUp();
            audioClipWalking.play();
        } else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
            setMoving();
            goDown();
            audioClipWalking.play();
        }

        if (keyEvent.getCode() == KeyCode.E) {
            setMelee();
            System.out.println("Melee!");
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            if(!knifeEquipped) {
                setShooting();
                audioClipFire.setVolume(0.25);
                audioClipFire.play();
                System.out.println("Fire!");
            } else {
                setMelee();
                System.out.println("Melee by space!");
            }
        } else if (keyEvent.getCode() == KeyCode.R && !knifeEquipped) {
            setReloading();
            audioClipReload.setVolume(0.25);
            audioClipReload.play();
            System.out.println("Reload!");
        } else if (keyEvent.getCode() == KeyCode.F) {
            playerAnimation("knife");
            System.out.println("Switched to knife");
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
            audioClipWalking.stop();
        } else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN
                || keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.S) {
            stopY();
            audioClipWalking.stop();
        }
    }
}