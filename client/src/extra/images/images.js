export default function Images(btoa){
	const svgSrc = svg => "data:image/svg+xml;base64,"+btoa(svg)
	const closeSvg = `
				<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 348.333 348.334" style="enable-background:new 0 0 348.333 348.334;" xml:space="preserve" fill="black">
					<path d="M336.559,68.611L231.016,174.165l105.543,105.549c15.699,15.705,15.699,41.145,0,56.85 c-7.844,7.844-18.128,11.769-28.407,11.769c-10.296,0-20.581-3.919-28.419-11.769L174.167,231.003L68.609,336.563 c-7.843,7.844-18.128,11.769-28.416,11.769c-10.285,0-20.563-3.919-28.413-11.769c-15.699-15.698-15.699-41.139,0-56.85   l105.54-105.549L11.774,68.611c-15.699-15.699-15.699-41.145,0-56.844c15.696-15.687,41.127-15.687,56.829,0l105.563,105.554   L279.721,11.767c15.705-15.687,41.139-15.687,56.832,0C352.258,27.466,352.258,52.912,336.559,68.611z"/>
				</svg>`;
	const noteSvg = `
	<svg fill="red" version="1.1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 490 490" style="enable-background:new 0 0 490 490;" xml:space="preserve">  
		<path d="M244.5,0C109.3,0,0,109.3,0,244.5S109.3,489,244.5,489S489,379.7,489,244.5S379.7,0,244.5,0z M244.5,448.4
				 c-112.4,0-203.9-91.5-203.9-203.9S132.1,40.6,244.5,40.6s203.9,91.5,203.9,203.9S356.9,448.4,244.5,448.4z" />
		<path d="M354.8,134.2c-8.3-8.3-20.8-8.3-29.1,0l-81.2,81.2l-81.1-81.1c-8.3-8.3-20.8-8.3-29.1,0s-8.3,20.8,0,29.1l81.1,81.1
				 l-81.1,81.1c-8.3,8.3-8.6,21.1,0,29.1c6.5,6,18.8,10.4,29.1,0l81.1-81.1l81.1,81.1c12.4,11.7,25,4.2,29.1,0
				 c8.3-8.3,8.3-20.8,0-29.1l-81.1-81.1l81.1-81.1C363.1,155,363.1,142.5,354.8,134.2z" />
	</svg>`;
	const closeSvgData = svgSrc(closeSvg)
	const noteSvgData = svgSrc(noteSvg)
	const arrowDownSvg =`<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" width="16px" height="16px" viewBox="0 0 306 306" xml:space="preserve">
							<polygon points="270.3,58.65 153,175.95 35.7,58.65 0,94.35 153,247.35 306,94.35"/>
						</svg>`
	const arrowDownSvgData=svgSrc(arrowDownSvg)
	const connectionSvg=`<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 285.269 285.269" style="enable-background:new 0 0 285.269 285.269;" xml:space="preserve"> 
						<path style="fill:'+fillColor+';" d="M272.867,198.634h-38.246c-0.333,0-0.659,0.083-0.986,0.108c-1.298-5.808-6.486-10.108-12.679-10.108 h-68.369c-7.168,0-13.318,5.589-13.318,12.757v19.243H61.553C44.154,220.634,30,206.66,30,189.262 c0-17.398,14.154-31.464,31.545-31.464l130.218,0.112c33.941,0,61.554-27.697,61.554-61.637s-27.613-61.638-61.554-61.638h-44.494 V14.67c0-7.168-5.483-13.035-12.651-13.035h-68.37c-6.193,0-11.381,4.3-12.679,10.108c-0.326-0.025-0.653-0.108-0.985-0.108H14.336 c-7.168,0-13.067,5.982-13.067,13.15v48.978c0,7.168,5.899,12.872,13.067,12.872h38.247c0.333,0,0.659-0.083,0.985-0.107 c1.298,5.808,6.486,10.107,12.679,10.107h68.37c7.168,0,12.651-5.589,12.651-12.757V64.634h44.494 c17.398,0,31.554,14.262,31.554,31.661c0,17.398-14.155,31.606-31.546,31.606l-130.218-0.04C27.612,127.862,0,155.308,0,189.248 s27.612,61.386,61.553,61.386h77.716v19.965c0,7.168,6.15,13.035,13.318,13.035h68.369c6.193,0,11.381-4.3,12.679-10.108 c0.327,0.025,0.653,0.108,0.986,0.108h38.246c7.168,0,12.401-5.982,12.401-13.15v-48.977 C285.269,204.338,280.035,198.634,272.867,198.634z M43.269,71.634h-24v-15h24V71.634z M43.269,41.634h-24v-15h24V41.634z M267.269,258.634h-24v-15h24V258.634z M267.269,228.634h-24v-15h24V228.634z"/>
					</svg>`;
	const connectionSvgData = svgSrc(connectionSvg)
	
	const folderSvg =`<svg version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" width="16px" height="16px" viewBox="0 0 510 510" style="enable-background:new 0 0 510 510;" xml:space="preserve">
					<path d="M204,51H51C22.95,51,0,73.95,0,102v306c0,28.05,22.95,51,51,51h408c28.05,0,51-22.95,51-51V153c0-28.05-22.95-51-51-51 H255L204,51z"/>
				</svg>`;
	const folderSvgData=svgSrc(folderSvg)
	
	const checkboxSvg = `<svg version="1.1" xmlns="http://www.w3.org/2000/svg" width="16px" viewBox="0 0 128.411 128.411">
					<polygon points="127.526,15.294 45.665,78.216 0.863,42.861 0,59.255 44.479,113.117 128.411,31.666"/>
				</svg>`;
	const checkboxSvgData=svgSrc(checkboxSvg)
	
	const calendarSvg = `<svg version="1.1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512;" xml:space="preserve">
				  <path style="fill:#FFFFFF;" d="M481.082,123.718V72.825c0-11.757-9.531-21.287-21.287-21.287H36         c-11.756,0-21.287,9.53-21.287,21.287v50.893L481.082,123.718L481.082,123.718z"/>
				  <g><path d="M481.082,138.431H14.713C6.587,138.431,0,131.843,0,123.718V72.825c0-19.85,16.151-36,36-36h423.793   c19.851,0,36,16.151,36,36v50.894C495.795,131.844,489.208,138.431,481.082,138.431z M29.426,109.005h436.942v-36.18   c0-3.625-2.949-6.574-6.574-6.574H36c-3.625,0-6.574,2.949-6.574,6.574V109.005z"/>
				  <path d="M144.238,282.415H74.93c-8.126,0-14.713-6.589-14.713-14.713v-61.765   c0-8.125,6.587-14.713,14.713-14.713h69.308c8.126,0,14.713,6.589,14.713,14.713c0,8.125-6.587,14.713-14.713,14.713H89.643v32.338   h54.595c8.126,0,14.713,6.589,14.713,14.713S152.364,282.415,144.238,282.415z"/></g>
				  <g><path d="M282.552,282.415h-69.308c-8.126,0-14.713-6.589-14.713-14.713v-61.765   c0-8.125,6.587-14.713,14.713-14.713h69.308c8.126,0,14.713,6.589,14.713,14.713v61.765   C297.265,275.826,290.678,282.415,282.552,282.415z M227.957,252.988h39.882V220.65h-39.882V252.988z"/>
				  <path d="M144.238,406.06H74.93c-8.126,0-14.713-6.589-14.713-14.713v-61.765   c0-8.125,6.587-14.713,14.713-14.713h69.308c8.126,0,14.713,6.589,14.713,14.713s-6.587,14.713-14.713,14.713H89.643v32.338h54.595   c8.126,0,14.713,6.589,14.713,14.713S152.364,406.06,144.238,406.06z"/></g>
				  <path d="M282.552,406.06h-69.308c-8.126,0-14.713-6.589-14.713-14.713v-61.765  c0-8.125,6.587-14.713,14.713-14.713h69.308c8.126,0,14.713,6.589,14.713,14.713v61.765  C297.265,399.471,290.678,406.06,282.552,406.06z M227.957,376.633h39.882v-32.338h-39.882V376.633z"/>
				  <g><path d="M420.864,282.415h-69.308c-8.126,0-14.713-6.589-14.713-14.713v-61.765   c0-8.125,6.587-14.713,14.713-14.713h69.308c8.126,0,14.713,6.589,14.713,14.713v61.765   C435.577,275.826,428.99,282.415,420.864,282.415z M366.269,252.988h39.882V220.65h-39.882V252.988L366.269,252.988z"/>
				  <path d="M99.532,92.878c-8.126,0-14.713-6.589-14.713-14.713V22.06c0-8.125,6.587-14.713,14.713-14.713   s14.713,6.589,14.713,14.713v56.106C114.245,86.291,107.658,92.878,99.532,92.878z"/>
				  <path d="M247.897,92.878c-8.126,0-14.713-6.589-14.713-14.713V22.06c0-8.125,6.587-14.713,14.713-14.713   s14.713,6.589,14.713,14.713v56.106C262.61,86.291,256.023,92.878,247.897,92.878z"/>
				  <path d="M396.263,92.878c-8.126,0-14.713-6.589-14.713-14.713V22.06c0-8.125,6.587-14.713,14.713-14.713   s14.713,6.589,14.713,14.713v56.106C410.976,86.291,404.389,92.878,396.263,92.878z"/>
				  <path d="M389.88,504.653c-67.338,0-122.12-54.782-122.12-122.12s54.782-122.12,122.12-122.12   c36.752,0,71.2,16.321,94.512,44.78c5.15,6.285,4.229,15.556-2.058,20.706c-6.285,5.148-15.556,4.229-20.706-2.058   c-17.7-21.608-43.851-33.999-71.747-33.999c-51.111,0-92.693,41.582-92.693,92.693s41.582,92.693,92.693,92.693   s92.693-41.582,92.693-92.693c0-8.125,6.587-14.713,14.713-14.713c8.126,0,14.713,6.589,14.713,14.713   C512,449.87,457.218,504.653,389.88,504.653z"/>
				  <path d="M228.475,490.606H36c-19.85,0-36-16.151-36-36V72.825c0-19.85,16.151-36,36-36h423.793   c19.851,0,36,16.151,36,36v164.701c0,8.125-6.587,14.713-14.713,14.713c-8.126,0-14.713-6.589-14.713-14.713V72.825   c0-3.625-2.949-6.574-6.574-6.574H36c-3.625,0-6.574,2.949-6.574,6.574v381.781c0,3.625,2.949,6.574,6.574,6.574h192.474   c8.126,0,14.713,6.589,14.713,14.713C243.187,484.018,236.601,490.606,228.475,490.606z"/></g>
				  <polyline style="fill:#FFFFFF;" points="429.606,382.533 389.88,382.533 389.88,342.808 "/>
				  <path d="M429.606,397.247H389.88c-8.126,0-14.713-6.589-14.713-14.713v-39.726  c0-8.125,6.587-14.713,14.713-14.713s14.713,6.589,14.713,14.713v25.012h25.012c8.126,0,14.713,6.589,14.713,14.713  S437.732,397.247,429.606,397.247z"/>
				  </svg>`;
	const calendarSvgData=svgSrc(calendarSvg)	
	
	const wifiSignalStateSvg =(l1C,l2C,l3C,l4C) => `
				<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" x="0px" y="0px" viewBox="0 0 147.586 147.586" style="enable-background:new 0 0 147.586 147.586;" xml:space="preserve">
					<path style="stroke:white;fill: ${l2C};" d="M48.712,94.208c-2.929,2.929-2.929,7.678,0,10.606c2.93,2.929,7.678,2.929,10.607,0   c7.98-7.98,20.967-7.98,28.947,0c1.465,1.464,3.385,2.197,5.304,2.197s3.839-0.732,5.304-2.197c2.929-2.929,2.929-7.678,0-10.606   C85.044,80.378,62.542,80.378,48.712,94.208z"></path>
					<path style="stroke:white;fill: ${l3C};" d="M26.73,72.225c-2.929,2.929-2.929,7.678,0,10.606s7.677,2.93,10.607,0   c20.102-20.102,52.811-20.102,72.912,0c1.465,1.464,3.385,2.197,5.304,2.197s3.839-0.732,5.304-2.197   c2.929-2.929,2.929-7.678,0-10.606C94.906,46.275,52.681,46.275,26.73,72.225z"></path>
					<path style="stroke:white;fill: ${l4C};" d="M145.39,47.692c-39.479-39.479-103.715-39.479-143.193,0c-2.929,2.929-2.929,7.678,0,10.606   c2.93,2.929,7.678,2.929,10.607,0c16.29-16.291,37.95-25.262,60.989-25.262s44.699,8.972,60.989,25.262   c1.465,1.464,3.385,2.197,5.304,2.197s3.839-0.732,5.304-2.197C148.319,55.37,148.319,50.621,145.39,47.692z"></path>
					<circle style="stroke:white;fill: ${l1C};" cx="73.793" cy="121.272" r="8.231"></circle>
				</svg>`;
	const wifiSignalStateSvgData = (l1C,l2C,l3C,l4C) => svgSrc(wifiSignalStateSvg(l1C,l2C,l3C,l4C))
	const beepMidi = `data:audio/mpeg;base64,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
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAP/7ksDVgAAAASwAAAAAAAAlgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/+5LA/4AAAAEsAAAAAAAAJYAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//uSwP+AAAAB
LAAAAAAAACWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAP/7ksD/gAAAASwAAAAAAAAlgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/+5LA/4AAAAEsAAAAAAAAJYAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//uSwP+AAAAB
LAAAAAAAACWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAP/7ksD/gAAAASwAAAAAAAAlgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/+5LA/4AAAAEsAAAAAAAAJYAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAgACAAIAAg
ACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAA
IAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAA//uSwP+AAAAB
LAAAAAAAACWAAAAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAg
ACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAA
IAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAg
ACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAA
IAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAg
ACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAA
IAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAgACAAIAAg
ACAAIAAgACAAAA==`
	
	
	return {
		closeSvgData,noteSvgData,arrowDownSvgData,connectionSvgData,folderSvgData,checkboxSvgData,calendarSvgData,
		wifiSignalStateSvgData,beepMidi
	}
}