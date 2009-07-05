package net.intensicode.core;

/**
 * TODO: Describe this!
 */
interface NetworkRequest
{
    NetworkCallback callback();

    void execute() throws Throwable;
}
