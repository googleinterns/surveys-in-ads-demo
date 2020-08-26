'use strict';

const e = React.createElement;


class CloseMenuButton extends React.Component{

  render(){
    return (
      e('div', {id: 'close_button_container'}, 
        e('img', {id:'close_menu_button', src:'../public/images/close.svg'
        , onMouseDown: this.props.handleMouseDown})
      )
    );
  }
}

class MenuButton extends React.Component {
  render() {
    return e(
      'img', 
      {id: 'menu_button', onMouseDown:this.props.handleMouseDown, 
      src:'../public/images/open-menu.svg'}
    );
  }
}


class Menu extends React.Component {
  render() {
    var visibility = "hide";
 
    if (this.props.menuVisibility) {
      visibility = "show";
    }
    return ( 
      e('div', {id: 'flyoutMenu', className: visibility}, 
        e(CloseMenuButton, {handleMouseDown : this.props.handleMouseDown}),
        e('a', {href: "../index.html"}, 'AdSense'), 
        e('a', {href: "gpt.html"}, 'GPT'),
        e('a', {href: "pubnet.html"}, 'PubNet'),
        e('a', {href: "rewarded_web.html"}, 'Rewarded Web')
      )
    );
  }
}

class MenuContainer extends React.Component {
  constructor(props, context) {
      super(props, context);
     
      this.state = {
        visible: false
      };
     
      this.handleMouseDown = this.handleMouseDown.bind(this);
      this.toggleMenu = this.toggleMenu.bind(this);

  }

  handleMouseDown(e) {
      this.toggleMenu();
      e.stopPropagation();
  }

  toggleMenu() {
      this.setState({
        visible: !this.state.visible
      });
  }

  render() {
    return ([ 
        e(MenuButton, {handleMouseDown: this.handleMouseDown}),
        e(Menu, {handleMouseDown: this.handleMouseDown, 
          menuVisibility: this.state.visible})             
    ]);
      
  }
}


const domContainer = document.querySelector('#menu_container');
ReactDOM.render(e(MenuContainer), domContainer);