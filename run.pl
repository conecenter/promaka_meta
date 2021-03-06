
### here is script to run different containers in runtime/production where most of sources do not present

use strict;

my $exec = sub{ print join(" ",@_),"\n"; exec @_; die 'exec failed' };

my @tasks;

my $serve = sub{
    $ENV{JAVA_TOOL_OPTIONS} = join " ", $ENV{JAVA_TOOL_OPTIONS},
        "-XX:+UseG1GC","-XX:MaxGCPauseMillis=200","-XX:+ExitOnOutOfMemoryError",
        "-XX:GCTimeRatio=1","-XX:MinHeapFreeRatio=15","-XX:MaxHeapFreeRatio=50";
    # https://www.javacodegeeks.com/2017/11/minimize-java-memory-usage-right-garbage-collector.html
    # with G1 unused RAM is released back to OS
    local $ENV{C4PUBLIC_PATH} = "htdocs";
    local $ENV{CLASSPATH} = join ":", sort <app/*.jar>;
    &$exec("sh", "serve.sh");
};
push @tasks, [main=>sub{
    m{([^/]+)$} and (-e $1 or symlink $_,$1) or die for </c4conf/*>; # gate do not need
    &$serve();
}];

my($cmd,@args)=@ARGV;
$cmd eq $$_[0] and $$_[1]->(@args) for @tasks;
