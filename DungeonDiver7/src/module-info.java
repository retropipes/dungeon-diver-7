module com.puttysoftware.dungeondiver7 {
	requires java.desktop;

	requires com.github.vorbis;
	requires com.puttysoftware.audio.ogg;
	requires com.puttysoftware.audio.wav;
	requires com.puttysoftware.avatarpicker;
	requires com.puttysoftware.diane;
	requires com.puttysoftware.fileutils;
	requires com.puttysoftware.help;
	requires com.puttysoftware.images;
	requires com.puttysoftware.integration;
	requires com.puttysoftware.picturepicker;
	requires com.puttysoftware.polytable;
	requires com.puttysoftware.randomrange;
	requires com.puttysoftware.storage;
	requires com.puttysoftware.updater;
	requires com.puttysoftware.xio;

	uses javax.sound.sampled.spi.AudioFileReader;
	uses javax.sound.sampled.spi.FormatConversionProvider;
}