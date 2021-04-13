package system;

import system.objects.Config;

final class Secret {
    static final String TOKEN = Config.get("TOKEN");
}
