package cn.moyen.spring.core.config;

import cn.jruyi.util.SystemProperties;

import static java.util.FormatProcessor.FMT;

public record Welcome(String name, double startCastTime, String startedTime, String url, int componentNum)
{
    @Override
    public String toString()
    {
        return
                FMT."""

                        ---------------------------------------------------------------------------
                            (♥◠‿◠)ﾉﾞ [ %s\{name()} ] runs successfully in %.2f\{startCastTime()}s!  ლ(´ڡ`ლ)ﾞ
                            %18s\{"Started time"}: %s\{startedTime()}
                            %18s\{"Document url"}: %s\{url()}/doc.html
                            %18s\{"Component number"}: %d\{componentNum()}
                            %18s\{"System info"}: %s\{SystemProperties.osName()} %s\{SystemProperties.osArch()}
                            %18s\{"Java version"}: %s\{SystemProperties.javaVersion()}
                            %18s\{"Java VM name"}: %s\{SystemProperties.jvmName()}
                        ---------------------------------------------------------------------------
                        """;
    }
}