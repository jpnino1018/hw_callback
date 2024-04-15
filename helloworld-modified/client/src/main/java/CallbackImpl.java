import com.zeroc.Ice.Current;

import Demo.Response;

public class CallbackImpl implements Demo.Callback{

    @Override
    public void callbackClient(Response response, Current current) {
        
        System.out.println("callback invoke "+response.value);
    }
    
}
