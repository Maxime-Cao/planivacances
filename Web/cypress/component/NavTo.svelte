<script lang="ts">
    import { useNavigate } from "svelte-navigator";

    const navigate = useNavigate();

    export let dest = "/";

    navigate(dest);

</script>