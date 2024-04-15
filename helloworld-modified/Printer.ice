module Demo
{
    class Response{
        long responseTime;
        string value;
    }

    interface Callback{
        void callbackClient(Response response);
    }

    interface Printer
    {
        void printString(string s, Callback* client);
    }


}