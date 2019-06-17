package zbar;



public class ZbarController {
    private static volatile ZbarController instance;

    static {
        System.loadLibrary("iconv");
        System.loadLibrary("zbarjni");
    }

    private ImageScanner scanner;

    public static ZbarController getInstance() {
        if (instance == null) {
            synchronized (ZbarController.class) {
                if (instance == null) {
                    instance = new ZbarController();
                }
            }
        }

        return instance;
    }

    private ZbarController() {
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
    }

    public String scan(byte[] data, int width, int height) {
        if (data == null || width <= 0 || height <= 0) {
            return null;
        }

        Image image = new Image(width, height, "Y800");
        image.setData(data);

        int result = scanner.scanImage(image);

        if (result != 0) {
            SymbolSet syms = scanner.getResults();
            for (Symbol sym : syms) {
                return sym.getData();
            }
        }
        return null;
    }
}
