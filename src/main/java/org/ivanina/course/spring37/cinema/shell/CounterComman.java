package org.ivanina.course.spring37.cinema.shell;

import org.ivanina.course.spring37.cinema.domain.Counter;
import org.ivanina.course.spring37.cinema.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.shell.standard.ShellComponent;

import java.util.stream.Collectors;

@ShellComponent
public class CounterComman implements CommandMarker {
    @Autowired
    @Qualifier("counterService")
    CounterService counterService;

    @CliAvailabilityIndicator({
            "getCounterMap",
            "getCounter"
    })
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = "getCounterMap", help = "Get all counter values")
    public String getCounterMap() {
        return "Counter Map:\n" + counterService.getMap().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n")) + "\n\n";
    }

    @CliCommand(value = "getCounter", help = "Get count by key")
    public String getCounter(
            @CliOption(key = {"key"}, mandatory = true, help = "Key of counter") final String key
    ) {
        Counter counter = counterService.getCounter(key);
        return counter == null ? "Does not exist Counter with KEY " + key : counter.toString();
    }
}
