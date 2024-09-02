<script lang="ts">
    import { isLoadingStore } from '../../stores/loading';

	let p: number = 0;
	let visible: boolean = false;

	function next() {
		p += 0.1;
		const remaining = 1 - p;
		if (remaining > 0.15) setTimeout(next, 500 / remaining);
	}

	isLoadingStore.subscribe(value => {
		visible = value;
		if(value) {
			p = 0;
			setTimeout(next, 250);
		}
	});

</script>

<div class="progress-container">
	{#if visible}
	<div class="progress" style="width: {p * 100}%" />
	{/if}
</div>


{#if p >= 0.4}
	<div class="fade" />
{/if}

<style>
	.progress-container {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 4px;
		z-index: 999;
	}

	.progress {
		position: absolute;
		left: 0;
		top: 0;
		height: 100%;
		background-color: #213547;
		transition: width 0.4s;
	}

	.fade {
		position: fixed;
		width: 100%;
		height: 100%;
		background-color: rgba(255, 255, 255, 0.3);
		pointer-events: none;
		z-index: 998;
		animation: fade 0.4s;
	}

	@keyframes fade {
		from {
			opacity: 0;
		}
		to {
			opacity: 1;
		}
	}
</style>
