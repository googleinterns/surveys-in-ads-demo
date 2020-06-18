'use strict';

const e = React.createElement;

class LikeButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = { liked: false };
    
  }


  render() {
    const props = { onClick: () => this.setState({ liked: true })};
    if (this.state.liked) {
      return 'You liked this.';
    }
  
    return e(
      'button',
      props,
      'Like'
    );
  }
}

const theScript = () => {
  this.StyleSheet
}



const domContainer = document.querySelector('#like_button_container');
ReactDOM.render(e(LikeButton), domContainer);