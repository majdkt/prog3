- Everything in the domainLogic have to be completely tested.
- New needed classes are Client, Server, ServerManager in separate Module
- Event systems and Listeners
- Capacity must be limited

Questions:
- Server und Client have access to Manager ?
- -------------------------------------------------------------------------
- Client connects to server and passes a Manager.
-> Server uses logic from ClientHandler to communicate with DomainLogic
- -------------------------------------------------------------------------
- Should I modify my CLI Menu so that the server Communicates with it
instead of communicating with domainLogic ? Nein
- 
- Should there be operations that can be called only from server side?
like specific CLI For the server ? Do we need CLI For Server at all ? Nein
- 
- Tips for event system. Writer and printer ? CLI and event system should be connected.

- Can I create new interfaces that make life easier? like what ?
you can edit the already exisitng interfaces
, you maybe actually should do it.x







I need an Interface in the domainlogic that combine somehow Uploadable and MediaContent interfaces







