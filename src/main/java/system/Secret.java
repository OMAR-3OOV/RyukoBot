package system;

import system.Objects.Config;

final class Secret {
    static final String TOKEN = Config.get("TOKEN");
}
