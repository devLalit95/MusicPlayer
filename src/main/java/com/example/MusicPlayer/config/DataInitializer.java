package com.example.MusicPlayer.config;

import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.SongRepository;
import com.example.MusicPlayer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, SongRepository songRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeSongs();
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            logger.info("Initializing sample users...");

            List<User> users = Arrays.asList(
                    createUser("lalit", "lalit@example.com", "root", User.Role.USER),
                    createUser("alice_smith", "alice@example.com", "alice123", User.Role.USER),
                    createUser("bob_johnson", "bob@example.com", "bob123", User.Role.USER),
                    createUser("admin", "admin@musicplayer.com", "admin123", User.Role.ADMIN)
            );

            userRepository.saveAll(users);
            logger.info("Created {} sample users", users.size());

            // Print login credentials for testing
            logger.info("=== Sample User Credentials ===");
            logger.info("User: john_doe / password123");
            logger.info("User: alice_smith / alice123");
            logger.info("User: bob_johnson / bob123");
            logger.info("Admin: admin / admin123");
            logger.info("===============================");
        }
    }

    private User createUser(String username, String email, String password, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return user;
    }

    private void initializeSongs() {
        if (songRepository.count() == 0) {
            logger.info("Initializing sample songs...");

            List<Song> songs = Arrays.asList(
                    createSong("Blinding Lights", "The Weeknd", "After Hours",
                            "https://example.com/songs/blinding_lights.mp3", 200),
                    createSong("Save Your Tears", "The Weeknd", "After Hours",
                            "https://example.com/songs/save_your_tears.mp3", 215),
                    createSong("Shape of You", "Ed Sheeran", "÷ (Divide)",
                            "https://example.com/songs/shape_of_you.mp3", 233),
                    createSong("Perfect", "Ed Sheeran", "÷ (Divide)",
                            "https://example.com/songs/perfect.mp3", 263),
                    createSong("Dance Monkey", "Tones and I", "The Kids Are Coming",
                            "https://example.com/songs/dance_monkey.mp3", 210),
                    createSong("Bad Guy", "Billie Eilish", "When We All Fall Asleep, Where Do We Go?",
                            "https://example.com/songs/bad_guy.mp3", 194),
                    createSong("Levitating", "Dua Lipa", "Future Nostalgia",
                            "https://example.com/songs/levitating.mp3", 203),
                    createSong("Don't Start Now", "Dua Lipa", "Future Nostalgia",
                            "https://example.com/songs/dont_start_now.mp3", 183),
                    createSong("Watermelon Sugar", "Harry Styles", "Fine Line",
                            "https://example.com/songs/watermelon_sugar.mp3", 174),
                    createSong("As It Was", "Harry Styles", "Harry's House",
                            "https://example.com/songs/as_it_was.mp3", 167),
                    createSong("Stay", "The Kid LAROI, Justin Bieber", "F*CK LOVE 3: OVER YOU",
                            "https://example.com/songs/stay.mp3", 141),
                    createSong("Peaches", "Justin Bieber", "Justice",
                            "https://example.com/songs/peaches.mp3", 198),
                    createSong("Flowers", "Miley Cyrus", "Endless Summer Vacation",
                            "https://example.com/songs/flowers.mp3", 200),
                    createSong("Wrecking Ball", "Miley Cyrus", "Bangerz",
                            "https://example.com/songs/wrecking_ball.mp3", 221),
                    createSong("Blinding Lights", "The Weeknd", "After Hours",
                            "https://example.com/songs/blinding_lights.mp3", 200),
                    createSong("Uptown Funk", "Mark Ronson ft. Bruno Mars", "Uptown Special",
                            "https://example.com/songs/uptown_funk.mp3", 269),
                    createSong("Thinking Out Loud", "Ed Sheeran", "x",
                            "https://example.com/songs/thinking_out_loud.mp3", 281),
                    createSong("Shallow", "Lady Gaga, Bradley Cooper", "A Star Is Born",
                            "https://example.com/songs/shallow.mp3", 215),
                    createSong("Rolling in the Deep", "Adele", "21",
                            "https://example.com/songs/rolling_in_the_deep.mp3", 228),
                    createSong("Someone Like You", "Adele", "21",
                            "https://example.com/songs/someone_like_you.mp3", 285)
            );

            songRepository.saveAll(songs);
            logger.info("Created {} sample songs", songs.size());
        }
    }

    private Song createSong(String title, String artist, String album, String fileUrl, int durationSeconds) {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setFileUrl(fileUrl);
        song.setDurationSeconds(durationSeconds);
        return song;
    }
}