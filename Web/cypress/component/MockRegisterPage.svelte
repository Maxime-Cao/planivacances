<script>
    import { Route, Router } from "svelte-navigator";
    import Contact from "../../src/lib/page/auth/Register.svelte";
    import NavTo from "./NavTo.svelte"
    import { writable } from "svelte/store";
    const counter = writable(0);

    let count = 0;

    counter.subscribe(value => {
        count = value;
    });
    
</script>

<Router>
    <Route>
        {#if count == 0}
            <NavTo dest="/connection"/>
            {counter.set(1)}
        {/if}

        <p id="confirmed">Enregistré</p>

    </Route>
    <Route path="/connection" component={Contact} />
</Router>