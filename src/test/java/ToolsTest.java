import org.cozz.tool.Tools;
import org.junit.Test;

public class ToolsTest {
    final byte[] bytes = "hello, world, yes, no , ok, foo, bar".getBytes();

    @Test
    public void indexOfTest() {
        int i = Tools.indexOf(bytes, "foo,");
        assert i == 28;

        i = Tools.indexOf(bytes, "foo..");
        assert i == -1;
    }

    @Test
    public void startsWithTest() {
        boolean rs = Tools.startsWith(bytes, "world");
        assert !rs;

        rs = Tools.startsWith(bytes, "hello, ");
        assert rs;

        rs = Tools.startsWith(bytes, "h");
        assert rs;
    }
}
