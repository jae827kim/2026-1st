package kliving.manager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import kliving.model.Answer;
import kliving.model.Post;
import kliving.model.User;
import kliving.util.DataUtil;

/**
 * Handles all file I/O for User.txt and Post.txt.
 * Implements UC-07 (Data Load), UC-08 (Data Save), UC-09 (Data Update).
 */
public class FileManager {

    private static final String DATA_DIR   = "data";
    private static final String USER_FILE  = DATA_DIR + "/User.txt";
    private static final String POST_FILE  = DATA_DIR + "/Post.txt";

    public FileManager() {
        new File(DATA_DIR).mkdirs();
        ensureFile(USER_FILE, buildDefaultAdmin());
        ensureFile(POST_FILE, "");
    }

    // ── UC-07 : Data Load ─────────────────────────────────────────────────

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = reader(USER_FILE)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                User u = parseUser(line);
                if (u != null) {
                    LabelManager.updateLabel(u);
                    users.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("[FileManager] User.txt 로드 실패: " + e.getMessage());
        }
        return users;
    }

    public List<Post> loadPosts() {
        List<Post> posts = new ArrayList<>();
        Post current = null;
        try (BufferedReader br = reader(POST_FILE)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("POST|")) {
                    current = parsePost(line);
                    if (current != null) posts.add(current);
                } else if (line.startsWith("ANSWER|") && current != null) {
                    Answer a = parseAnswer(line);
                    if (a != null) current.addAnswer(a);
                }
            }
        } catch (IOException e) {
            System.err.println("[FileManager] Post.txt 로드 실패: " + e.getMessage());
        }
        return posts;
    }

    // ── UC-08 : Data Save ─────────────────────────────────────────────────

    public boolean saveUsers(List<User> users) {
        try (BufferedWriter bw = writer(USER_FILE)) {
            for (User u : users) {
                bw.write(u.toFileString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("[FileManager] User.txt 저장 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean savePosts(List<Post> posts) {
        try (BufferedWriter bw = writer(POST_FILE)) {
            for (Post p : posts) {
                bw.write(p.toFileString());
                bw.newLine();
                for (Answer a : p.getAnswers()) {
                    bw.write(a.toFileString());
                    bw.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("[FileManager] Post.txt 저장 실패: " + e.getMessage());
            return false;
        }
    }

    // ── Parsers ───────────────────────────────────────────────────────────

    private User parseUser(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 8) return null;
        try {
            return new User(
                dec(p[0]), dec(p[1]), dec(p[2]),
                toInt(p[3]), toInt(p[4]), toInt(p[5]),
                toBool(p[6]), toBool(p[7])
            );
        } catch (Exception e) {
            System.err.println("[FileManager] 사용자 파싱 오류: " + line);
            return null;
        }
    }

    private Post parsePost(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 10) return null;
        try {
            String selId = dec(p[8]);
            return new Post(
                dec(p[1]), dec(p[2]), dec(p[3]), dec(p[4]),
                toInt(p[5]), dec(p[6]),
                toBool(p[7]),
                selId.isEmpty() ? null : selId,
                toBool(p[9])
            );
        } catch (Exception e) {
            System.err.println("[FileManager] 게시글 파싱 오류: " + line);
            return null;
        }
    }

    private Answer parseAnswer(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 7) return null;
        try {
            return new Answer(
                dec(p[1]), dec(p[2]), dec(p[3]), dec(p[4]),
                toBool(p[5]), toBool(p[6])
            );
        } catch (Exception e) {
            System.err.println("[FileManager] 답변 파싱 오류: " + line);
            return null;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static String dec(String s)   { return DataUtil.decode(s); }
    private static int    toInt(String s)  { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }
    private static boolean toBool(String s){ return "true".equalsIgnoreCase(s.trim()); }

    private BufferedReader reader(String path) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
    }

    private BufferedWriter writer(String path) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, false), StandardCharsets.UTF_8));
    }

    private void ensureFile(String path, String defaultContent) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
                if (!defaultContent.isEmpty()) {
                    try (BufferedWriter bw = writer(path)) {
                        bw.write(defaultContent);
                        bw.newLine();
                    }
                }
            } catch (IOException e) {
                System.err.println("[FileManager] 파일 생성 실패: " + path);
            }
        }
    }

    private String buildDefaultAdmin() {
        // admin|admin123|관리자|10|0|0|false|true
        User admin = new User("admin", "admin123", "관리자", 10, 0, 0, false, true);
        LabelManager.updateLabel(admin);
        return admin.toFileString();
    }
}
