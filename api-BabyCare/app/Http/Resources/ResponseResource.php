<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ResponseResource extends JsonResource
{
    public $status;
    public $code;
    public $message;
    public $data;
    public $fiturUsers;

    public function __construct($code, $message, $data = null)
    {
        if ($code >= 300) {
            $this->status = false;
        }else {
            $this->status = true;
        }
        $this->code = $code;
        $this->message = $message;
        $this->data = empty($data) ? (object)[] : $data;
        $this->fiturUsers = [];
        // parent::__construct($data);
    }

    public function toArray($request) {
        return [
            'code' => $this->code,
            'status' => $this->status,
            'message' => $this->message,
            'data' => $this->data,
        ];
    }

    public function toResponse($request)
    {
        return response()->json($this->toArray($request), $this->code);
    }

    /**
     * Set the FiturUser array for the response.
     *
     * @param  array  $fiturUsers
     * @return void
     */
    public function setFiturUsers($fiturUsers)
    {
        $this->fiturUsers = $fiturUsers;
    }
}
