<script>
    import { Route, Router } from "svelte-navigator";
    import Contact from "../../src/lib/page/Contact.svelte";
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
            <NavTo dest="/contact"/>
            {counter.set(1)}
        {/if}

        <p id="confirmed">Mail contact envoyé</p>

    </Route>
    <Route path="/contact" component={Contact} />
</Router>