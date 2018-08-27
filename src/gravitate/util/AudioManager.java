package gravitate.util;
// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Largely based off this
//https://www.geeksforgeeks.org/play-audio-file-using-java/

public class AudioManager {

	// to store current position
	Long currentFrame;
	Clip clip;

	// current status of clip
	String status;

	AudioInputStream audioInputStream;
	static String filePath;
	private static ClassLoader classLoader;

	// constructor to initialize streams and clip
	public AudioManager(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		classLoader = Thread.currentThread().getContextClassLoader();
		filePath = path;
		// create AudioInputStream object
		audioInputStream = AudioSystem.getAudioInputStream(classLoader.getResource(filePath));

		// create clip reference
		clip = AudioSystem.getClip();

		// open audioInputStream to the clip
		clip.open(audioInputStream);

		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Plays the audio track.
	 */
		public void play() {
		// start the clip
		clip.start();

		status = "play";
	}

		/**
		 * Pauses the audio track.
		 */
	public void pause() {
		if (status.equals("paused")) {
			return;
		}
		this.currentFrame = this.clip.getMicrosecondPosition();
		clip.stop();
		status = "paused";
	}

	/**
	 * Resumes the audio track.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (status.equals("play")) {
			return;
		}
		clip.close();
		resetAudioStream();
		clip.setMicrosecondPosition(currentFrame);
		this.play();
	}
	
	/**
	 * Stops the audio track.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		currentFrame = 0L;
		clip.stop();
		clip.close();
	}

	/**
	 * Resets the audio stream.
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

}