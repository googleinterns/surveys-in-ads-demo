import Bling from "react-gpt";
var GPT = Bling;
'use strict';

class GPT_ads_resize extends React.Component {

    constructor(props) {
        super(props);
        this.state = { width: 0, height: 0 };
        this.updateWindowDimensions = this.updateWindowDimensions.bind(this);
      }
      
      componentDidMount() {
        this.updateWindowDimensions();
        window.addEventListener('resize', this.updateWindowDimensions);
      }
      
      componentWillUnmount() {
        window.removeEventListener('resize', this.updateWindowDimensions);
      }
      
      updateWindowDimensions() {
        this.setState({ width: window.innerWidth, height: window.innerHeight });
      }

      render(){
          if (this.state.width < 950) {
              return e(
                GPT, 
                {
                    adUnitPath: "/22068421158/gpt_03", 
                    sizeMapping: "[{viewport: [0, 0], slot: [320, 50]},{viewport: [750, 0], slot: [728, 90]},{viewport: [1050, 0], slot: [1024, 120]}]"
                }
            );
          } else {
            e(
                GPT, 
                {
                    adUnitPath: "/22068421158/gpt_02", 
                    sizeMapping: "[{viewport: [0, 0], slot: [320, 50]},{viewport: [750, 0], slot: [728, 90]},{viewport: [1050, 0], slot: [1024, 120]}]"
                }
            );
          }
      }
  }

// class GPT_ads_resize extends React.Component {
//     render() {
//         // return (
//         //     <GPT
//         //         adUnitPath='/22068421158/gpt_03'
//         //         sizeMapping={[
//         //             {viewport: [0, 0], slot: [320, 50]},
//         //             {viewport: [750, 0], slot: [728, 90]},
//         //             {viewport: [1050, 0], slot: [1024, 120]}
//         //         ]}
//         //     />
//         // );
//         return e(
//             GPT, 
//             {
//                 adUnitPath: "/22068421158/gpt_03", 
//                 sizeMapping: "[{viewport: [0, 0], slot: [320, 50]},{viewport: [750, 0], slot: [728, 90]},{viewport: [1050, 0], slot: [1024, 120]}]"
//             }
//         );
//     }
// }


console.log("Started");
const adContainer = document.querySelector('#banner');
ReactDOM.render(e(GPT_ads_resize), adContainer);


