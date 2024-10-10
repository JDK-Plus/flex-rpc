package flex.server.service;


import flex.common.provider.GreetingService;

public class GreetingServiceImpl implements GreetingService {

    @Override
    public String hello(String name) {
        return String.format("Hello, %s!", name);
    }
}
