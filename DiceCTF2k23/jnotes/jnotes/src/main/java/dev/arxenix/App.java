package dev.arxenix;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Cookie;

public class App {
    // The default note is a constant
    public static String DEFAULT_NOTE = "Hello world!\r\nThis is a simple note-taking app.";

    public static String getNote(Context ctx) {
        // Working with ctx is a bit weird, so we have to do this
        var note = ctx.cookie("note");
        if (note == null) {
            setNote(ctx, DEFAULT_NOTE);
            return DEFAULT_NOTE;
        }
        // URL decoding the note whyy is this necessary 
        return URLDecoder.decode(note, StandardCharsets.UTF_8);
    }

    public static void setNote(Context ctx, String note) {
        note = URLEncoder.encode(note, StandardCharsets.UTF_8);
        // The note is set as a cookie
        ctx.cookie(new Cookie("note", note, "/", -1, false, 0, true));
    }

    public static void main(String[] args) {
        var app = Javalin.create();

        app.get("/", ctx -> {
            var note = getNote(ctx);
            ctx.html("""
                    <html>
                    <head></head>
                    <body>
                    <h1>jnotes</h1>

                    <form method="post" action="create">
                    <textarea rows="20" cols="50" name="note">
                    // Here we are inserting our note, any sanitization no I can't see 
                    %s
                    </textarea>
                    <br>
                    <button type="submit">Save notes</button>
                    </form>

                    <hr style="margin-top: 10em">
                    <footer>
                    <i>see something unusual on our site? report it <a href="https://adminbot.mc.ax/web-jnotes">here</a></i>
                    </footer>
                    </body>
                    </html>""".formatted(note));
        });

        app.post("/create", ctx -> {
            var note = ctx.formParam("note");
            setNote(ctx, note);
            ctx.redirect("/");
        });
        
        app.start(1337);
    }
}

// <script>
// const query=document.querySelector("name");

// fetch("ENDPOINT_")
// </script>

