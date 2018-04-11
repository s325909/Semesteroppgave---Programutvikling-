package entities;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;

public class Player extends Movable {

    private String equippedWeapon;

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
            equippedWeapon = "knife";
        } else if (animationWanted == "handgun") {
            setSpriteIdle("/resources/Art/Survivor/handgun/idle/survivor-idle_handgun_", ".png", 20);
            setSpriteMoving("/resources/Art/Survivor/handgun/move/survivor-move_handgun_", ".png", 20);
            setSpriteMelee("/resources/Art/Survivor/handgun/meleeattack/survivor-meleeattack_handgun_", ".png", 15);
            setSpriteShooting("/resources/Art/Survivor/handgun/shoot/survivor-shoot_handgun_", ".png", 3);
            setSpriteReloading("/resources/Art/Survivor/handgun/reload/survivor-reload_handgun_", ".png", 15);
            equippedWeapon = "handgun";
        } else if (animationWanted == "rifle") {
            setSpriteIdle("/resources/Art/Survivor/rifle/idle/survivor-idle_rifle_", ".png", 20);
            setSpriteMoving("/resources/Art/Survivor/rifle/move/survivor-move_rifle_", ".png", 20);
            setSpriteMelee("/resources/Art/Survivor/rifle/meleeattack/survivor-meleeattack_rifle_", ".png", 15);
            setSpriteShooting("/resources/Art/Survivor/rifle/shoot/survivor-shoot_rifle_", ".png", 3);
            setSpriteReloading("/resources/Art/Survivor/rifle/reload/survivor-reload_rifle_", ".png", 20);
            equippedWeapon = "rifle";
        }
    }

    AudioClip audioClipFire = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/pistol_shot.wav").toExternalForm());
    AudioClip audioClipReload = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/pistol_reload.mp3").toExternalForm());
    AudioClip audioClipWalking = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/footsteps_single.wav").toExternalForm());

    AudioClip audioClipRifleFire = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/rifle_shot.wav").toExternalForm());
    AudioClip audioClipRifleReload = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/rifle_reload.mp3").toExternalForm());

    AudioClip audioClipKnifeSwish = new AudioClip(this.getClass().getResource("/resources/Sound/Sound Effects/knife_swish.mp3").toExternalForm());

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
            if (equippedWeapon == "knife") {
                audioClipKnifeSwish.setVolume(0.25);
                audioClipKnifeSwish.play();
            }
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            if(equippedWeapon != "knife") {
                setShooting();
                System.out.println("Fire!");

                if (equippedWeapon == "handgun") {
                    audioClipFire.setVolume(0.25);
                    audioClipFire.play();
                } else if (equippedWeapon == "rifle") {
                    audioClipRifleFire.setVolume(0.25);
                    audioClipRifleFire.play();
                }
            } else {
                setMelee();
                audioClipKnifeSwish.setVolume(0.25);
                audioClipKnifeSwish.play();
                System.out.println("Melee by space!");
            }
        } else if (keyEvent.getCode() == KeyCode.R && equippedWeapon != "knife") {
            setReloading();
            System.out.println("Reload!");

            if (equippedWeapon == "handgun") {
                audioClipReload.setVolume(0.25);
                audioClipReload.play();
            } else if (equippedWeapon == "rifle") {
                audioClipRifleReload.setVolume(0.25);
                audioClipRifleReload.play();
            }
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