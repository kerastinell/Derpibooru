package derpibooru.derpy.server.parsers.objects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import derpibooru.derpy.server.TestResourceLoader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserScriptParserObjectTest {
    private UserScriptParserObject loggedInScript;
    private UserScriptParserObject loggedOutScript;

    @Before
    public void setUp() {
        TestResourceLoader loader = new TestResourceLoader();
        String loggedIn = loader.readTestResourceFile("UserScriptLoggedIn.js");
        loggedInScript = new UserScriptParserObject(loggedIn);
        String loggedOut = loader.readTestResourceFile("UserScriptLoggedOut.js");
        loggedOutScript = new UserScriptParserObject(loggedOut);
    }

    @Test
    public void testUsername() {
        assertThat(loggedInScript.getUsername(), is("TestUserName = Test"));
        assertThat(loggedOutScript.getUsername(), is(""));
    }

    @Test
    public void testAvatarUrl() {
        assertThat(loggedInScript.getAvatarUrl(), is("https://derpicdn.net/avatars/examplePath.png"));
        assertThat(loggedOutScript.getAvatarUrl(), is("https://derpicdn.net/assets/no_avatar.svg"));
    }

    @Test
    public void testFilterId() {
        assertThat(loggedInScript.getFilterId(), is(100073));
        assertThat(loggedOutScript.getFilterId(), is(100073));
    }

    @Test
    public void testSpoileredTagIds() {
        assertThat(loggedInScript.getSpoileredTagIds(), is(Arrays.asList(
                41133,41161,42773,114937,173118,173119,173120,173121,173122,173123,173124)));
        assertThat(loggedOutScript.getSpoileredTagIds(), is(Arrays.asList(
                41133,41161,42773,114937,173118,173119,173120,173121,173122,173123,173124)));
    }
}
