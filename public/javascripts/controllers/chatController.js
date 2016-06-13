edflow

    .controller("chatController", function ($scope, $rootScope, $http, $state) {

        $scope.chatItems = [
            {name: "Robot", message: "Hello. This is a link recommendation system."},
            {name: "Robot", message: "Main - list of newly added links. Flows is a sources for links. And not only domains, but all pages where i can find updatable list. Mine is a your links."},
            {name: "Robot", message: "and this is me. I will recommend links for you and even can interact with you via this chat. You should feed me. The more i eat, the better recommendation will be. Throw links here or use chrome extension."}
        ];

        $scope.placeholder = "";

        console.log("chat");

        $rootScope.showChat = function () {
            $scope.placeholder = "Write a message...";
            document.querySelector("#chat ul").style.display = "inline";
            document.querySelector("body data").style.display = "none";

            document.querySelector("#chat i.icon").style.display = "none";
            document.querySelector("#chat input").style.opacity = "1";
            document.querySelector("#chat input").focus();
            window.scrollTo(0, document.body.scrollHeight);
        }

        $rootScope.hideChat = function () {

            if ($rootScope.selectedCategories.length === 0)
                return;

            $scope.placeholder = "";
            document.querySelector("#chat ul").style.display = "none";
            document.querySelector("body data").style.display = "inline";

            document.querySelector("#chat i.icon").style.display = "inline";
            document.querySelector("#chat input").style.opacity = "0.8";
            document.querySelector("#chat input").blur();
        }

        $scope.send = function () {

            var value = document.querySelector("#chat input").value;

            if (value.length === 0)
                return;

            console.log("send");
            $scope.chatItems.push({name: "You", message: value, self: true});
            document.querySelector("#chat input").value = "";

            $http.get("api/chat?message=" + value).success(function (data) {

            }).error(function () {
                    $rootScope.circleAnimation = "error";
                });
        }

        $rootScope.showChat();
    })

    .directive('ngEnter', function() {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                        scope.$eval(attrs.ngEnter, {'event': event});
                    });

                    event.preventDefault();
                }
            });
        };
    });
